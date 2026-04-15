package com.sssupply.customerportal.service.impl;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.entity.Comment;
import com.sssupply.customerportal.entity.KbArticle;
import com.sssupply.customerportal.entity.Ticket;
import com.sssupply.customerportal.enums.TicketCategory;
import com.sssupply.customerportal.enums.TicketPriority;
import com.sssupply.customerportal.repository.CommentRepository;
import com.sssupply.customerportal.repository.KbArticleRepository;
import com.sssupply.customerportal.repository.TicketRepository;
import com.sssupply.customerportal.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final KbArticleRepository kbArticleRepository;
    private final ObjectMapper objectMapper;

    // ==================== classify ====================

    @Override
    public AiClassifyResponse classifyTicket(AiClassifyRequest request) {
        String prompt = """
                You are a support ticket classifier. Analyse the ticket and respond ONLY with a JSON object.
                No markdown, no explanation — raw JSON only.

                Ticket Title: %s
                Ticket Description: %s

                Valid categories: %s
                Valid priorities: %s

                Respond in this exact JSON format:
                {
                  "suggestedCategory": "BUG",
                  "suggestedPriority": "HIGH",
                  "suggestedTags": ["tag1", "tag2"],
                  "confidence": 0.91
                }
                """.formatted(
                request.getTitle(),
                request.getDescription(),
                Arrays.toString(TicketCategory.values()),
                Arrays.toString(TicketPriority.values())
        );

        String response = chatClient.prompt(prompt).call().content();

        try {
            JsonNode json = objectMapper.readTree(cleanJson(response));
            return AiClassifyResponse.builder()
                    .suggestedCategory(TicketCategory.valueOf(json.get("suggestedCategory").asText()))
                    .suggestedPriority(TicketPriority.valueOf(json.get("suggestedPriority").asText()))
                    .suggestedTags(readStringList(json, "suggestedTags"))
                    .confidence(new BigDecimal(json.get("confidence").asText()))
                    .similarTicketIds(new ArrayList<>())
                    .build();
        } catch (Exception e) {
            log.warn("AI classify parse failed, returning defaults. Raw: {}", response, e);
            return AiClassifyResponse.builder()
                    .suggestedCategory(TicketCategory.OTHER)
                    .suggestedPriority(TicketPriority.MEDIUM)
                    .suggestedTags(List.of())
                    .confidence(BigDecimal.ZERO)
                    .similarTicketIds(List.of())
                    .build();
        }
    }

    // ==================== suggest-response ====================

    @Override
    public AiTextResponse suggestResponse(AiSuggestResponseRequest request) {
        Ticket ticket = findTicket(request.getTicketId());
        List<Comment> comments = commentRepository
                .findByTicketIdAndIsInternalFalseAndDeletedAtIsNullOrderByCreatedAtAsc(ticket.getId());

        String thread = comments.stream()
                .map(c -> c.getAuthor().getName() + ": " + c.getBody())
                .collect(Collectors.joining("\n"));

        String prompt = """
                You are a helpful support agent assistant.
                Based on the following ticket and conversation thread, suggest a professional reply.

                Ticket Title: %s
                Description: %s
                Category: %s
                Priority: %s

                Conversation so far:
                %s

                Write a concise, empathetic reply to the customer.
                """.formatted(ticket.getTitle(), ticket.getDescription(),
                ticket.getCategory(), ticket.getPriority(), thread.isBlank() ? "(no comments yet)" : thread);

        return AiTextResponse.builder()
                .result(chatClient.prompt(prompt).call().content())
                .build();
    }

    // ==================== summarize-thread ====================

    @Override
    public AiTextResponse summarizeThread(UUID ticketId) {
        Ticket ticket = findTicket(ticketId);
        List<Comment> comments = commentRepository
                .findByTicketIdAndIsInternalFalseAndDeletedAtIsNullOrderByCreatedAtAsc(ticketId);

        String thread = comments.stream()
                .map(c -> c.getAuthor().getName() + ": " + c.getBody())
                .collect(Collectors.joining("\n"));

        String prompt = """
                Summarise the following support ticket conversation in 3-5 bullet points.
                Be concise and factual.

                Ticket: %s
                Thread:
                %s
                """.formatted(ticket.getTitle(), thread.isBlank() ? "(no messages)" : thread);

        return AiTextResponse.builder()
                .result(chatClient.prompt(prompt).call().content())
                .build();
    }

    // ==================== summarize-ticket ====================

    @Override
    public AiTextResponse summarizeTicket(UUID ticketId) {
        Ticket ticket = findTicket(ticketId);

        String prompt = """
                Give a one-paragraph TL;DR summary of this support ticket.

                Title: %s
                Description: %s
                Category: %s
                Priority: %s
                Status: %s
                """.formatted(ticket.getTitle(), ticket.getDescription(),
                ticket.getCategory(), ticket.getPriority(), ticket.getStatus());

        return AiTextResponse.builder()
                .result(chatClient.prompt(prompt).call().content())
                .build();
    }

    // ==================== search ====================

    @Override
    public List<TicketResponse> search(AiSearchRequest request) {
        // Use PostgreSQL full-text search; AI enhancement can be layered on top later
        List<Ticket> tickets = ticketRepository.searchByText(request.getQuery());
        return tickets.stream()
                .map(this::mapTicketToResponse)
                .collect(Collectors.toList());
    }

    // ==================== auto-route ====================

    @Override
    public AiTextResponse autoRoute(UUID ticketId) {
        Ticket ticket = findTicket(ticketId);

        String prompt = """
                You are a support routing assistant. Based on this ticket, suggest the best team or agent type to handle it.
                Be concise — one or two sentences.

                Title: %s
                Category: %s
                Priority: %s
                Description: %s
                """.formatted(ticket.getTitle(), ticket.getCategory(),
                ticket.getPriority(), ticket.getDescription());

        return AiTextResponse.builder()
                .result(chatClient.prompt(prompt).call().content())
                .build();
    }

    // ==================== predict-resolution-time ====================

    @Override
    public AiPredictResolutionResponse predictResolutionTime(UUID ticketId) {
        Ticket ticket = findTicket(ticketId);

        String prompt = """
                Estimate the resolution time in hours for this support ticket.
                Respond ONLY with a JSON object — no markdown, no explanation.

                Title: %s
                Category: %s
                Priority: %s

                JSON format:
                { "estimatedHours": 4.5, "reasoning": "Brief reason here." }
                """.formatted(ticket.getTitle(), ticket.getCategory(), ticket.getPriority());

        String response = chatClient.prompt(prompt).call().content();

        try {
            JsonNode json = objectMapper.readTree(cleanJson(response));
            return AiPredictResolutionResponse.builder()
                    .estimatedHours(new BigDecimal(json.get("estimatedHours").asText()))
                    .reasoning(json.get("reasoning").asText())
                    .build();
        } catch (Exception e) {
            log.warn("AI predict parse failed. Raw: {}", response, e);
            return AiPredictResolutionResponse.builder()
                    .estimatedHours(new BigDecimal("8.0"))
                    .reasoning("Could not parse AI response. Default estimate provided.")
                    .build();
        }
    }

    // ==================== kb-search ====================

    @Override
    public List<KbArticleResponse> kbSearch(String query) {
        // Fetch published articles and filter/rank by relevance using AI
        List<KbArticle> articles = kbArticleRepository.findByPublishedTrueAndDeletedAtIsNull();
        if (articles.isEmpty()) return List.of();

        String articleList = articles.stream()
                .map(a -> "ID:%s | TITLE:%s | CATEGORY:%s".formatted(a.getId(), a.getTitle(), a.getCategory()))
                .collect(Collectors.joining("\n"));

        String prompt = """
                Given this user query, return the IDs of the most relevant KB articles (max 5), ordered by relevance.
                Respond ONLY with a JSON array of UUIDs — no markdown, no explanation.
                Example: ["uuid1","uuid2"]

                Query: %s

                Available articles:
                %s
                """.formatted(query, articleList);

        String response = chatClient.prompt(prompt).call().content();

        try {
            JsonNode jsonArray = objectMapper.readTree(cleanJson(response));
            List<UUID> rankedIds = new ArrayList<>();
            jsonArray.forEach(node -> rankedIds.add(UUID.fromString(node.asText())));

            return rankedIds.stream()
                    .flatMap(id -> articles.stream().filter(a -> a.getId().equals(id)))
                    .map(this::mapKbToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("AI KB search parse failed. Raw: {}", response, e);
            return articles.stream().limit(5).map(this::mapKbToResponse).collect(Collectors.toList());
        }
    }

    // ==================== generate-report-summary ====================

    @Override
    public AiTextResponse generateReportSummary(String period) {
        String prompt = """
                Generate a professional support operations summary report for the period: %s.
                Include trends, SLA compliance highlights, and recommendations.
                Keep it to 3-5 paragraphs.
                (Note: use your general knowledge — live metrics integration is a future enhancement.)
                """.formatted(period != null ? period : "this month");

        return AiTextResponse.builder()
                .result(chatClient.prompt(prompt).call().content())
                .build();
    }

    // ==================== Helpers ====================

    private Ticket findTicket(UUID id) {
        return ticketRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }

    private String cleanJson(String raw) {
        if (raw == null) return "{}";
        // Strip markdown code fences if present
        return raw.replaceAll("```json", "").replaceAll("```", "").trim();
    }

    private List<String> readStringList(JsonNode json, String field) {
        List<String> list = new ArrayList<>();
        if (json.has(field) && json.get(field).isArray()) {
            json.get(field).forEach(node -> list.add(node.asText()));
        }
        return list;
    }

    private TicketResponse mapTicketToResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber() != null ? "TKT-" + ticket.getTicketNumber() : null)
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .category(ticket.getCategory())
                .priority(ticket.getPriority())
                .status(ticket.getStatus())
                .createdByName(ticket.getCreatedBy().getName())
                .assignedToName(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : null)
                .tags(ticket.getTags() != null ? List.of(ticket.getTags()) : null)
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }

    private KbArticleResponse mapKbToResponse(KbArticle article) {
        return KbArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .category(article.getCategory())
                .published(article.isPublished())
                .helpfulCount(article.getHelpfulCount())
                .notHelpfulCount(article.getNotHelpfulCount())
                .createdByName(article.getCreatedBy().getName())
                .workspaceId(article.getWorkspace() != null ? article.getWorkspace().getId() : null)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
}
