package com.sssupply.customerportal.service;
import com.sssupply.customerportal.dto.*;
import java.util.List;
import java.util.UUID;
public interface EscalationService {
    EscalationRuleResponse createRule(EscalationRuleRequest request);
    List<EscalationRuleResponse> getRules();
    void deleteRule(UUID id);
}
