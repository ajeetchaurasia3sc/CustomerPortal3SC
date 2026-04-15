package com.sssupply.customerportal.service;
import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.enums.*;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;
public interface TicketService {
    TicketResponse createTicket(TicketCreateRequest request);
    Page<TicketResponse> getAllTickets(Pageable pageable, TicketStatus status, TicketPriority priority, UUID projectId);
    TicketResponse getTicketById(UUID id);
    TicketResponse updateTicket(UUID id, TicketUpdateRequest request);
    void deleteTicket(UUID id);
    TicketResponse updateStatus(UUID id, TicketStatusUpdateRequest request);
    TicketResponse assignTicket(UUID id, UUID agentId);
    List<AttachmentResponse> uploadAttachments(UUID ticketId, List<MultipartFile> files);
    List<TicketHistoryResponse> getTicketHistory(UUID ticketId);
    SlaStatusResponse getSlaStatus(UUID ticketId);
    List<CommentResponse> getComments(UUID ticketId);
    CommentResponse addComment(UUID ticketId, CommentRequest request);
    InternalNoteResponse addInternalNote(UUID ticketId, InternalNoteRequest request);
    List<InternalNoteResponse> getInternalNotes(UUID ticketId);
}
