package com.sssupply.customerportal.service.impl;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.entity.*;
import com.sssupply.customerportal.enums.*;
import com.sssupply.customerportal.repository.*;
import com.sssupply.customerportal.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final TicketStatusHistoryRepository ticketStatusHistoryRepository;
    private final CommentRepository commentRepository;

    @Override
    public TicketResponse createTicket(TicketCreateRequest request) {
        User currentUser = getCurrentUser();
        Ticket ticket = Ticket.builder().title(request.getTitle()).description(request.getDescription())
                .category(request.getCategory()).priority(request.getPriority()).status(TicketStatus.OPEN)
                .createdBy(currentUser).workspace(currentUser.getWorkspace())
                .tags(request.getTags() != null ? request.getTags().toArray(new String[0]) : null).build();
        return mapToResponse(ticketRepository.save(ticket));
    }

    @Override @Transactional(readOnly = true)
    public Page<TicketResponse> getAllTickets(Pageable pageable, TicketStatus status, TicketPriority priority, UUID projectId) {
        return ticketRepository.findAllWithFilters(status, priority, projectId, pageable).map(this::mapToResponse);
    }

    @Override @Transactional(readOnly = true)
    public TicketResponse getTicketById(UUID id) {
        return mapToResponse(ticketRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found")));
    }

    @Override
    public TicketResponse updateTicket(UUID id, TicketUpdateRequest request) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        if (request.getTitle() != null) ticket.setTitle(request.getTitle());
        if (request.getCategory() != null) ticket.setCategory(request.getCategory());
        if (request.getPriority() != null) ticket.setPriority(request.getPriority());
        if (request.getTags() != null) ticket.setTags(request.getTags().toArray(new String[0]));
        return mapToResponse(ticketRepository.save(ticket));
    }

    @Override
    public void deleteTicket(UUID id) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setDeletedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
    }

    @Override
    public TicketResponse updateStatus(UUID id, TicketStatusUpdateRequest request) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        TicketStatus oldStatus = ticket.getStatus();
        TicketStatus newStatus = request.getStatus();
        if (oldStatus != newStatus) {
            ticketStatusHistoryRepository.save(TicketStatusHistory.builder().ticket(ticket)
                    .fromStatus(oldStatus).toStatus(newStatus).changedBy(getCurrentUser())
                    .note("Status updated via API").build());
        }
        ticket.setStatus(newStatus);
        if (newStatus == TicketStatus.RESOLVED) ticket.setResolvedAt(LocalDateTime.now());
        if (newStatus == TicketStatus.CLOSED) ticket.setClosedAt(LocalDateTime.now());
        return mapToResponse(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponse assignTicket(UUID id, UUID agentId) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        ticket.setAssignedTo(agent);
        return mapToResponse(ticketRepository.save(ticket));
    }

    @Override
    public List<AttachmentResponse> uploadAttachments(UUID ticketId, List<MultipartFile> files) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        User currentUser = getCurrentUser();
        List<AttachmentResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty() || file.getSize() > 25 * 1024 * 1024)
                throw new RuntimeException("File too large or empty");
            String s3Key = "attachments/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            Attachment attachment = attachmentRepository.save(Attachment.builder().ticket(ticket)
                    .uploadedBy(currentUser).filename(file.getOriginalFilename()).s3Key(s3Key)
                    .mimeType(file.getContentType()).sizeBytes(file.getSize()).build());
            responses.add(AttachmentResponse.builder().id(attachment.getId()).filename(attachment.getFilename())
                    .mimeType(attachment.getMimeType()).sizeBytes(attachment.getSizeBytes())
                    .uploadedByName(currentUser.getName()).createdAt(attachment.getCreatedAt()).build());
        }
        return responses;
    }

    @Override @Transactional(readOnly = true)
    public List<TicketHistoryResponse> getTicketHistory(UUID ticketId) {
        return ticketStatusHistoryRepository.findByTicketIdOrderByCreatedAtDesc(ticketId).stream()
                .map(h -> TicketHistoryResponse.builder().id(h.getId()).fromStatus(h.getFromStatus())
                        .toStatus(h.getToStatus()).changedByName(h.getChangedBy().getName())
                        .note(h.getNote()).createdAt(h.getCreatedAt()).build())
                .collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public SlaStatusResponse getSlaStatus(UUID ticketId) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        return SlaStatusResponse.builder().breached(ticket.isSlaBreached()).slaDueAt(ticket.getSlaDueAt())
                .hoursRemaining(48L).status(ticket.isSlaBreached() ? "BREACHED" : "ON_TRACK").build();
    }

    @Override @Transactional(readOnly = true)
    public List<CommentResponse> getComments(UUID ticketId) {
        return commentRepository.findByTicketIdAndIsInternalFalseAndDeletedAtIsNullOrderByCreatedAtAsc(ticketId)
                .stream().map(this::mapToCommentResponse).collect(Collectors.toList());
    }

    @Override
    public CommentResponse addComment(UUID ticketId, CommentRequest request) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));
        Comment comment = commentRepository.save(Comment.builder().ticket(ticket)
                .author(getCurrentUser()).body(request.getBody()).isInternal(request.isInternal()).build());
        return mapToCommentResponse(comment);
    }

    @Override
    public InternalNoteResponse addInternalNote(UUID ticketId, InternalNoteRequest request) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().name().startsWith("INTERNAL"))
            throw new RuntimeException("Only 3SC team members can add internal notes");
        Comment note = commentRepository.save(Comment.builder().ticket(ticket).author(currentUser)
                .body(request.getBody()).isInternal(true).build());
        return InternalNoteResponse.builder().id(note.getId()).ticketId(ticketId)
                .authorName(currentUser.getName()).body(note.getBody()).createdAt(note.getCreatedAt()).build();
    }

    @Override @Transactional(readOnly = true)
    public List<InternalNoteResponse> getInternalNotes(UUID ticketId) {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().name().startsWith("INTERNAL"))
            throw new RuntimeException("Access denied");
        return commentRepository.findByTicketIdAndIsInternalTrueAndDeletedAtIsNullOrderByCreatedAtAsc(ticketId)
                .stream().map(c -> InternalNoteResponse.builder().id(c.getId()).ticketId(ticketId)
                        .authorName(c.getAuthor().getName()).body(c.getBody()).createdAt(c.getCreatedAt()).build())
                .collect(Collectors.toList());
    }

    private TicketResponse mapToResponse(Ticket t) {
        return TicketResponse.builder().id(t.getId())
                .ticketNumber(t.getTicketNumber() != null ? "TKT-" + t.getTicketNumber() : null)
                .title(t.getTitle()).description(t.getDescription()).category(t.getCategory())
                .priority(t.getPriority()).status(t.getStatus())
                .createdByName(t.getCreatedBy().getName())
                .assignedToName(t.getAssignedTo() != null ? t.getAssignedTo().getName() : null)
                .tags(t.getTags() != null ? List.of(t.getTags()) : null)
                .createdAt(t.getCreatedAt()).updatedAt(t.getUpdatedAt()).build();
    }

    private CommentResponse mapToCommentResponse(Comment c) {
        boolean canEdit = c.getCreatedAt() != null && LocalDateTime.now().minusMinutes(5).isBefore(c.getCreatedAt());
        return CommentResponse.builder().id(c.getId()).ticketId(c.getTicket().getId())
                .authorName(c.getAuthor().getName()).authorRole(c.getAuthor().getRole().name())
                .body(c.getBody()).isInternal(c.isInternal()).createdAt(c.getCreatedAt())
                .editedAt(c.getEditedAt()).isEdited(c.getEditedAt() != null).canEdit(canEdit).build();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new RuntimeException("Unauthenticated");
        return userRepository.findByEmailAndDeletedAtIsNull(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + auth.getName()));
    }
}
