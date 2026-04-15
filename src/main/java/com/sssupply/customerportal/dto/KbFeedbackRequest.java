package com.sssupply.customerportal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KbFeedbackRequest {

    @NotNull(message = "helpful field is required (true or false)")
    private Boolean helpful;
}
