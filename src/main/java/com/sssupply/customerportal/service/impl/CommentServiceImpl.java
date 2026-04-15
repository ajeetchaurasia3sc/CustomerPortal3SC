package com.sssupply.customerportal.service.impl;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.entity.*;
import com.sssupply.customerportal.repository.*;
import com.sssupply.customerportal.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
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
    public CommentResponse updateComment(UUID commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + commentId));
        User currentUser = getCurrentUser();
        if (!comment.getAuthor().getId().equals(currentUser.getId()))
            throw new RuntimeException("You can only edit your own comments");
        if (LocalDateTime.now().minusMinutes(5).isAfter(comment.getCreatedAt()))
            throw new RuntimeException("Edit window (5 minutes) has expired");
        comment.setBody(request.getBody());
        comment.setEditedAt(LocalDateTime.now());
        return mapToCommentResponse(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + commentId));
        User currentUser = getCurrentUser();
        boolean isOwner = comment.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().name().contains("INTERNAL");
        if (!isOwner && !isAdmin) throw new RuntimeException("Access denied");
        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Override
    public InternalNoteResponse addInternalNote(UUID ticketId, InternalNoteRequest request) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().name().startsWith("INTERNAL"))
            throw new RuntimeException("Only internal team members can add notes");
        Comment note = commentRepository.save(Comment.builder().ticket(ticket)
                .author(currentUser).body(request.getBody()).isInternal(true).build());
        return InternalNoteResponse.builder().id(note.getId()).ticketId(ticketId)
                .authorName(currentUser.getName()).body(note.getBody()).createdAt(note.getCreatedAt()).build();
    }

    @Override
    public List<InternalNoteResponse> getInternalNotes(UUID ticketId) {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().name().startsWith("INTERNAL"))
            throw new RuntimeException("Access denied");
        return commentRepository.findByTicketIdAndIsInternalTrueAndDeletedAtIsNullOrderByCreatedAtAsc(ticketId)
                .stream().map(c -> InternalNoteResponse.builder().id(c.getId()).ticketId(ticketId)
                        .authorName(c.getAuthor().getName()).body(c.getBody()).createdAt(c.getCreatedAt()).build())
                .collect(Collectors.toList());
    }

    private CommentResponse mapToCommentResponse(Comment c) {
        boolean canEdit = c.getCreatedAt() != null && LocalDateTime.now().minusMinutes(5).isBefore(c.getCreatedAt());
        return CommentResponse.builder().id(c.getId()).ticketId(c.getTicket().getId())
                .authorName(c.getAuthor().getName()).authorRole(c.getAuthor().getRole().name())
                .body(c.getBody()).isInternal(c.isInternal()).createdAt(c.getCreatedAt())
                .editedAt(c.getEditedAt()).isEdited(c.getEditedAt() != null).canEdit(canEdit).build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
