package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.enums.*;
import com.sssupply.customerportal.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Tag(name = "Ticket")
@RestController @RequestMapping("/api/v1/tickets") @RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(@RequestBody TicketCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.createTicket(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TicketResponse>>> getAllTickets(
            Pageable pageable,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(required = false) UUID projectId) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.getAllTickets(pageable, status, priority, projectId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.getTicketById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicket(@PathVariable UUID id, @RequestBody TicketUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.updateTicket(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTicket(@PathVariable UUID id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TicketResponse>> updateStatus(@PathVariable UUID id, @RequestBody TicketStatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.updateStatus(id, request)));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<TicketResponse>> assignTicket(@PathVariable UUID id, @RequestBody TicketAssignRequest request) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.assignTicket(id, request.getAgentId())));
    }

    @PostMapping("/{id}/attachments")
    public ResponseEntity<ApiResponse<List<AttachmentResponse>>> uploadAttachments(
            @PathVariable UUID id, @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.uploadAttachments(id, files)));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<TicketHistoryResponse>>> getTicketHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.getTicketHistory(id)));
    }

    @GetMapping("/{id}/sla-status")
    public ResponseEntity<ApiResponse<SlaStatusResponse>> getSlaStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.getSlaStatus(id)));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.getComments(id)));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(@PathVariable UUID id, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.addComment(id, request)));
    }

    @PostMapping("/{id}/internal-notes")
    public ResponseEntity<ApiResponse<InternalNoteResponse>> addInternalNote(@PathVariable UUID id, @RequestBody InternalNoteRequest request) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.addInternalNote(id, request)));
    }

    @GetMapping("/{id}/internal-notes")
    public ResponseEntity<ApiResponse<List<InternalNoteResponse>>> getInternalNotes(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.getInternalNotes(id)));
    }
}
