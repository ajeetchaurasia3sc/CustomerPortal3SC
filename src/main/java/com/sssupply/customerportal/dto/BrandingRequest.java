package com.sssupply.customerportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandingRequest {

    /** Full URL or base64-encoded image */
    private String logoUrl;

    /** Hex colour, e.g. #2563EB */
    private String primaryColor;

    private String accentColor;
}
