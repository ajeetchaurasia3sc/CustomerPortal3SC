package com.sssupply.customerportal.service.impl;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.entity.*;
import com.sssupply.customerportal.repository.*;
import com.sssupply.customerportal.service.SlaRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class SlaRuleServiceImpl implements SlaRuleService {
    private final SlaRuleRepository slaRuleRepository;
    private final UserRepository userRepository;

    @Override @Transactional(readOnly = true)
    public List<SlaRuleResponse> getAllSlaRules() {
        return slaRuleRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override @Transactional
    public SlaRuleResponse createSlaRule(SlaRuleRequest request) {
        SlaRule rule = new SlaRule();
        updateEntityFromRequest(rule, request);
        return mapToResponse(slaRuleRepository.save(rule));
    }

    @Override @Transactional
    public SlaRuleResponse updateSlaRule(UUID id, SlaRuleRequest request) {
        SlaRule rule = slaRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA Rule not found: " + id));
        updateEntityFromRequest(rule, request);
        return mapToResponse(slaRuleRepository.save(rule));
    }

    private SlaRuleResponse mapToResponse(SlaRule e) {
        return SlaRuleResponse.builder().id(e.getId()).name(e.getName()).priority(e.getPriority())
                .category(e.getCategory()).firstResponseHours(e.getFirstResponseHours())
                .resolutionHours(e.getResolutionHours()).warnAtPercent(e.getWarnAtPercent())
                .escalateOnBreach(e.isEscalateOnBreach())
                .escalateToUserId(e.getEscalateToUser() != null ? e.getEscalateToUser().getId() : null)
                .createdAt(e.getCreatedAt()).updatedAt(e.getUpdatedAt()).build();
    }

    private void updateEntityFromRequest(SlaRule e, SlaRuleRequest r) {
        e.setName(r.getName()); e.setPriority(r.getPriority()); e.setCategory(r.getCategory());
        e.setFirstResponseHours(r.getFirstResponseHours()); e.setResolutionHours(r.getResolutionHours());
        e.setWarnAtPercent(r.getWarnAtPercent()); e.setEscalateOnBreach(r.isEscalateOnBreach());
        if (r.getEscalateToUserId() != null) {
            e.setEscalateToUser(userRepository.findById(r.getEscalateToUserId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + r.getEscalateToUserId())));
        } else { e.setEscalateToUser(null); }
    }
}
