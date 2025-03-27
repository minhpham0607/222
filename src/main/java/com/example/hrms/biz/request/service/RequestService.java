package com.example.hrms.biz.request.service;

import com.example.hrms.biz.request.model.Request;
import com.example.hrms.biz.request.model.criteria.RequestCriteria;
import com.example.hrms.biz.request.model.dto.RequestDto;
import com.example.hrms.biz.request.repository.RequestMapper;
import com.example.hrms.common.http.criteria.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RequestService {
    private final RequestMapper requestMapper;

    public RequestService(RequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }

    public int count(RequestCriteria criteria) {
        log.info("Counting requests with criteria: {}", criteria);
        return requestMapper.count(criteria);
    }

    public List<RequestDto.Resp> list(Page page, RequestCriteria criteria) {
        page.validate();
        log.info("Fetching request list with criteria: {}", criteria);
        try {
            List<Request> requests = requestMapper.select(
                    page.getPageSize(),
                    page.getOffset(),
                    criteria.getRequestId(),
                    criteria.getUsername(),
                    criteria.getDepartmentId(),
                    criteria.getRequestType(),
                    criteria.getRequestReason(),
                    criteria.getRequestStatus(),
                    criteria.getApproverUsername(),
                    criteria.getStartTime(),
                    criteria.getEndTime()
            );
            log.info("Number of requests fetched: {}", requests.size());
            return requests.stream().map(RequestDto.Resp::toResponse).toList();
        } catch (Exception e) {
            log.error("Error fetching request list", e);
            throw new RuntimeException("Could not fetch request list, please try again later.");
        }
    }
    public int getTotalLeaveDays(String username) {
        log.info("Fetching total leave days for user: {}", username);
        Integer totalDays = requestMapper.calculateTotalLeaveDays(username);
        log.info("Total leave days for {}: {}", username, totalDays);
        return totalDays != null ? totalDays : 0;
    }
    public boolean createRequest(String username, RequestDto.Req requestDto) {
        Long departmentId = requestMapper.findDepartmentByUsername(username);
        if (departmentId == null) return false;

        String approverUsername = requestMapper.findLatestApproverByDepartment(departmentId);

        Request newRequest = new Request();
        newRequest.setUsername(username);
        newRequest.setDepartmentId(departmentId);
        newRequest.setRequestType(requestDto.getRequestType());  // 🟢 Đảm bảo đúng lớp
        newRequest.setRequestReason(requestDto.getRequestReason());
        newRequest.setRequestStatus(requestDto.getRequestStatus());
        newRequest.setApproverUsername(approverUsername);
        newRequest.setStartTime(requestDto.getStartTime());
        newRequest.setEndTime(requestDto.getEndTime());

        requestMapper.insertRequest(newRequest);
        return true;
    }
    public boolean updateRequest(Request request) {
        return requestMapper.updateRequest(request) > 0;
    }
}