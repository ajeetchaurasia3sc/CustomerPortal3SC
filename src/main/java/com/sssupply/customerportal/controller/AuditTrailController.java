package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.service.AuditTrailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Tag(name = "Audit Trail")
@RestController @RequestMapping("/api/v1/audit-trail") @RequiredArgsConstructor
public class AuditTrailController {
    private final AuditTrailService auditTrailService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<ApiResponse<List<AuditTrailResponse>>> getAuditTrail(@PathVariable UUID ticketId) {
        return ResponseEntity.ok(ApiResponse.success(auditTrailService.getAuditTrailByTicketId(ticketId)));
    }
}
