package com.sssupply.customerportal.service;
import com.sssupply.customerportal.dto.*;
import java.util.List;
import java.util.UUID;
public interface CommentService {
    List<CommentResponse> getComments(UUID ticketId);
    CommentResponse addComment(UUID ticketId, CommentRequest request);
    CommentResponse updateComment(UUID commentId, CommentRequest request);
    void deleteComment(UUID commentId);
    InternalNoteResponse addInternalNote(UUID ticketId, InternalNoteRequest request);
    List<InternalNoteResponse> getInternalNotes(UUID ticketId);
}
