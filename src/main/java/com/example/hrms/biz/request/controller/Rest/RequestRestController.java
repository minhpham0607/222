package com.example.hrms.biz.request.controller.Rest;

import com.example.hrms.biz.request.model.Request;
import com.example.hrms.biz.request.model.criteria.RequestCriteria;
import com.example.hrms.biz.request.model.dto.RequestDto;
import com.example.hrms.biz.request.service.RequestService;
import com.example.hrms.common.http.criteria.Page;
import com.example.hrms.common.http.model.ResultPageData;
import com.example.hrms.enumation.RequestStatusEnum;
import com.example.hrms.enumation.RequestTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "API requests")
@RestController
@RequestMapping("/api/v1/requests")
public class RequestRestController {
    private final RequestService requestService;

    public RequestRestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @Operation(summary = "List requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RequestDto.Resp.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @GetMapping("")
    public ResultPageData<List<RequestDto.Resp>> list(Page page, RequestCriteria criteria) {
        int total = requestService.count(criteria);
        ResultPageData<List<RequestDto.Resp>> response = new ResultPageData<>(criteria, total);
        if (total > 0) {
            response.setResultData(requestService.list(page, criteria));
        } else {
            response.setResultData(Collections.emptyList());
        }
        return response;
    }
    @Operation(summary = "Create a new request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Request created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })

    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody RequestDto.Req requestDto, @RequestParam String username) {
        boolean success = requestService.createRequest(username, requestDto);

        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create request");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Request created successfully", "username", username));
    }
    @Operation(summary = "Get total leave days of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved total leave days",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @GetMapping("/days-off")
    public long getTotalLeaveDays(@RequestParam String username) {
        return requestService.getTotalLeaveDays(username);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRequest(@PathVariable Long id, @RequestBody Request request) {
        request.setRequestId(id);
        boolean updated = requestService.updateRequest(request);
        if (updated) {
            return ResponseEntity.ok("Request updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Request not found.");
        }
    }


}