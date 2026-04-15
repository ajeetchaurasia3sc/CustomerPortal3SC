package com.sssupply.customerportal.service;

import com.sssupply.customerportal.dto.KbArticleRequest;
import com.sssupply.customerportal.dto.KbArticleResponse;
import com.sssupply.customerportal.dto.KbFeedbackRequest;

import java.util.List;
import java.util.UUID;

public interface KbArticleService {

    List<KbArticleResponse> listArticles();

    KbArticleResponse createArticle(KbArticleRequest request);

    KbArticleResponse getArticleById(UUID id);

    KbArticleResponse updateArticle(UUID id, KbArticleRequest request);

    void deleteArticle(UUID id);

    KbArticleResponse submitFeedback(UUID id, KbFeedbackRequest request);
}
