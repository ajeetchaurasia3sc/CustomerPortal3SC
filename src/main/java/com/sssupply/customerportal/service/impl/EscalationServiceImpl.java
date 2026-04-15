package com.sssupply.customerportal.service.impl;
import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.service.EscalationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
@Service @RequiredArgsConstructor
public class EscalationServiceImpl implements EscalationService {
    @Override
    public EscalationRuleResponse createRule(EscalationRuleRequest request) {
        return EscalationRuleResponse.builder().id(UUID.randomUUID()).name(request.getName())
                .priority(request.getPriority()).category(request.getCategory())
                .escalateAfterMinutes(request.getEscalateAfterMinutes())
                .escalateToUserId(request.getEscalateToUserId()).build();
    }
    @Override public List<EscalationRuleResponse> getRules() { return new ArrayList<>(); }
    @Override public void deleteRule(UUID id) {}
}
