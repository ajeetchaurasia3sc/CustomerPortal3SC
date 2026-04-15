package com.sssupply.customerportal.service.impl;

import com.sssupply.customerportal.dto.KbArticleRequest;
import com.sssupply.customerportal.dto.KbArticleResponse;
import com.sssupply.customerportal.dto.KbFeedbackRequest;
import com.sssupply.customerportal.entity.KbArticle;
import com.sssupply.customerportal.entity.User;
import com.sssupply.customerportal.repository.KbArticleRepository;
import com.sssupply.customerportal.repository.UserRepository;
import com.sssupply.customerportal.service.KbArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KbArticleServiceImpl implements KbArticleService {

    private final KbArticleRepository kbArticleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<KbArticleResponse> listArticles() {
        User currentUser = getCurrentUser();
        // 3SC staff can see all (including unpublished); customers only see published
        boolean isStaff = currentUser.getRole().name().startsWith("INTERNAL");
        if (isStaff) {
            return kbArticleRepository.findByPublishedTrueAndDeletedAtIsNull()
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
        return kbArticleRepository
                .findByWorkspaceIdOrWorkspaceIdIsNullAndPublishedTrueAndDeletedAtIsNull(
                        currentUser.getWorkspace().getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public KbArticleResponse createArticle(KbArticleRequest request) {
        User currentUser = getCurrentUser();

        KbArticle article = KbArticle.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .category(request.getCategory())
                .published(request.isPublished())
                .helpfulCount(0)
                .notHelpfulCount(0)
                .createdBy(currentUser)
                .workspace(currentUser.getWorkspace())
                .build();

        article = kbArticleRepository.save(article);
        return mapToResponse(article);
    }

    @Override
    @Transactional(readOnly = true)
    public KbArticleResponse getArticleById(UUID id) {
        KbArticle article = findById(id);
        return mapToResponse(article);
    }

    @Override
    public KbArticleResponse updateArticle(UUID id, KbArticleRequest request) {
        KbArticle article = findById(id);

        if (request.getTitle() != null) article.setTitle(request.getTitle());
        if (request.getBody() != null) article.setBody(request.getBody());
        if (request.getCategory() != null) article.setCategory(request.getCategory());
        article.setPublished(request.isPublished());

        article = kbArticleRepository.save(article);
        return mapToResponse(article);
    }

    @Override
    public void deleteArticle(UUID id) {
        KbArticle article = findById(id);
        article.setDeletedAt(LocalDateTime.now());
        article.setPublished(false);
        kbArticleRepository.save(article);
    }

    @Override
    public KbArticleResponse submitFeedback(UUID id, KbFeedbackRequest request) {
        KbArticle article = findById(id);

        if (Boolean.TRUE.equals(request.getHelpful())) {
            article.setHelpfulCount(article.getHelpfulCount() + 1);
        } else {
            article.setNotHelpfulCount(article.getNotHelpfulCount() + 1);
        }

        article = kbArticleRepository.save(article);
        return mapToResponse(article);
    }

    // ==================== Helpers ====================

    private KbArticle findById(UUID id) {
        return kbArticleRepository.findById(id)
                .filter(a -> a.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("KB article not found with id: " + id));
    }

    private KbArticleResponse mapToResponse(KbArticle article) {
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

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
