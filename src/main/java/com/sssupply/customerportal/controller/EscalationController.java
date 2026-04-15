package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.service.EscalationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Tag(name = "Escalations")
@RestController @RequestMapping("/api/v1/escalations") @RequiredArgsConstructor
public class EscalationController {
    private final EscalationService escalationService;

    @PostMapping("/rules")
    public ResponseEntity<ApiResponse<EscalationRuleResponse>> createRule(@RequestBody EscalationRuleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(escalationService.createRule(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EscalationRuleResponse>>> listRules() {
        return ResponseEntity.ok(ApiResponse.success(escalationService.getRules()));
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRule(@PathVariable UUID id) {
        escalationService.deleteRule(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
