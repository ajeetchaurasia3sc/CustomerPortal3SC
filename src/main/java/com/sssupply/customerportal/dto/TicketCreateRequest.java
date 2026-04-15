package com.sssupply.customerportal.dto;
import com.sssupply.customerportal.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TicketCreateRequest {
    @NotBlank @Size(max = 200) private String title;
    @NotBlank private String description;
    @NotNull private TicketCategory category;
    @NotNull private TicketPriority priority;
    private UUID projectId;
    private List<String> tags;
}
