package com.sssupply.customerportal.service;
import com.sssupply.customerportal.dto.*;
import java.util.List;
import java.util.UUID;
public interface SlaRuleService {
    List<SlaRuleResponse> getAllSlaRules();
    SlaRuleResponse createSlaRule(SlaRuleRequest request);
    SlaRuleResponse updateSlaRule(UUID id, SlaRuleRequest request);
}
