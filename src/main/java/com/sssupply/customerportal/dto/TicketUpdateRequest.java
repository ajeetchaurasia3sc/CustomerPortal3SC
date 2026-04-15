package com.sssupply.customerportal.dto;
import com.sssupply.customerportal.enums.*;
import lombok.*;
import java.util.List;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TicketUpdateRequest {
    private String title;
    private TicketCategory category;
    private TicketPriority priority;
    private List<String> tags;
    private UUID projectId;
}
