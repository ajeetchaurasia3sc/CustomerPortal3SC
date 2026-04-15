package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.service.SlaRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Tag(name = "SLA Rules")
@RestController @RequestMapping("/api/v1/sla/rules") @RequiredArgsConstructor
public class SlaRuleController {
    private final SlaRuleService slaRuleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD','INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<List<SlaRuleResponse>>> getAllSlaRules() {
        return ResponseEntity.ok(ApiResponse.success(slaRuleService.getAllSlaRules()));
    }

    @PostMapping
    @PreAuthorize("hasRole('INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<SlaRuleResponse>> createSlaRule(@RequestBody SlaRuleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(slaRuleService.createSlaRule(request)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<SlaRuleResponse>> updateSlaRule(@PathVariable UUID id, @RequestBody SlaRuleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(slaRuleService.updateSlaRule(id, request)));
    }
}
