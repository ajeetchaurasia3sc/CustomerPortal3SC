package com.sssupply.customerportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KbArticleResponse {

    private UUID id;
    private String title;
    private String body;
    private String category;
    private boolean published;
    private int helpfulCount;
    private int notHelpfulCount;
    private String createdByName;
    private UUID workspaceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
