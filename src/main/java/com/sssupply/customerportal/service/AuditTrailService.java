package com.sssupply.customerportal.service;
import com.sssupply.customerportal.dto.AuditTrailResponse;
import java.util.List;
import java.util.UUID;
public interface AuditTrailService {
    List<AuditTrailResponse> getAuditTrailByTicketId(UUID ticketId);
}
