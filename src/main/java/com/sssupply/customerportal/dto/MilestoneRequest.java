package com.sssupply.customerportal.dto;

import com.sssupply.customerportal.enums.MilestoneStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneRequest {

    @NotBlank(message = "Milestone title is required")
    @Size(max = 200)
    private String title;

    private LocalDate dueDate;

    private MilestoneStatus status;
}
