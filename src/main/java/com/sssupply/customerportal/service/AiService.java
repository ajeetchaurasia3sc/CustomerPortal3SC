package com.sssupply.customerportal.service;

import com.sssupply.customerportal.dto.*;

import java.util.List;
import java.util.UUID;

public interface AiService {

    AiClassifyResponse classifyTicket(AiClassifyRequest request);

    AiTextResponse suggestResponse(AiSuggestResponseRequest request);

    AiTextResponse summarizeThread(UUID ticketId);

    AiTextResponse summarizeTicket(UUID ticketId);

    List<TicketResponse> search(AiSearchRequest request);

    AiTextResponse autoRoute(UUID ticketId);

    AiPredictResolutionResponse predictResolutionTime(UUID ticketId);

    List<KbArticleResponse> kbSearch(String query);

    AiTextResponse generateReportSummary(String period);
}
