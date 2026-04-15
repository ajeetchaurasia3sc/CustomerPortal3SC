package com.sssupply.customerportal.service.impl;
import com.sssupply.customerportal.dto.AuditTrailResponse;
import com.sssupply.customerportal.service.AuditTrailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
@Service @RequiredArgsConstructor
public class AuditTrailServiceImpl implements AuditTrailService {
    @Override
    public List<AuditTrailResponse> getAuditTrailByTicketId(UUID ticketId) {
        // TODO: implement from ticket_status_history
        return new ArrayList<>();
    }
}
