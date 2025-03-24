package com.example.hrms.biz.meetingroom.controller.rest;

import com.example.hrms.biz.booking.model.criteria.BookingCriteria;
import com.example.hrms.biz.meetingroom.model.criteria.MeetingRoomCriteria;
import com.example.hrms.biz.meetingroom.model.dto.MeetingRoomDTO;
import com.example.hrms.biz.meetingroom.service.MeetingRoomService;
import com.example.hrms.common.http.criteria.Page;
import com.example.hrms.common.http.model.ResultPageData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Tag(name = "Advertisement API v1")
@RestController
@RequestMapping("/api/v1/meeting-room")
public class MeetingRoomRestController {
    private final MeetingRoomService meetingRoomService;

    public MeetingRoomRestController(MeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @Operation(summary="List room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MeetingRoomDTO.Resp.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @GetMapping("")
    public ResultPageData<List<MeetingRoomDTO.Resp>> list(Page page, MeetingRoomCriteria mCriteria) {
        int total = meetingRoomService.count(mCriteria);
        ResultPageData<List<MeetingRoomDTO.Resp>> response = new ResultPageData<>(mCriteria, total);
        if (total > 0) {
            response.setResultData(meetingRoomService.list(page, mCriteria));
        } else {
            response.setResultData(Collections.emptyList());
        }
        return response;
    }
}
