package com.example.hrms.biz.meetingroom.service;

import com.example.hrms.biz.meetingroom.model.MeetingRoom;
import com.example.hrms.biz.meetingroom.model.criteria.MeetingRoomCriteria;
import com.example.hrms.biz.meetingroom.model.dto.MeetingRoomDTO;
import com.example.hrms.biz.meetingroom.repository.MeetingRoomMapper;
import com.example.hrms.common.http.criteria.Page;
import com.example.hrms.utils.MeetingRoomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MeetingRoomServiceTest {

    @Mock
    private MeetingRoomMapper mapper;

    @InjectMocks
    private MeetingRoomService service;

    public MeetingRoomServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCount() {
        MeetingRoomCriteria criteria = new MeetingRoomCriteria();
        when(mapper.count(criteria)).thenReturn(5);

        int total = service.count(criteria);
        assertEquals(5, total, "Expected count of meeting rooms to be 5.");
        verify(mapper, times(1)).count(criteria);
    }

    @Test
    void testList() {
        Page page = new Page();
        page.setPageNo(1);
        page.setPageSize(2);

        MeetingRoomCriteria criteria = new MeetingRoomCriteria();

        // Mocking data using the builder pattern
        List<MeetingRoom> mockRooms = new ArrayList<>();
        mockRooms.add(new MeetingRoomUtils.Builder()
                .roomId(1L)
                .roomName("Room A")
                .location("Floor 1")
                .capacity(10)
                .username("User A")
                .startTime(LocalDateTime.of(2025, 3, 1, 10, 0))
                .endTime(LocalDateTime.of(2025, 3, 1, 12, 0))
                .status(null)
                .buildMeetingRoom());
        mockRooms.add(new MeetingRoomUtils.Builder()
                .roomId(2L)
                .roomName("Room B")
                .location("Floor 2")
                .capacity(15)
                .username("User B")
                .startTime(LocalDateTime.of(2025, 3, 2, 14, 0))
                .endTime(LocalDateTime.of(2025, 3, 2, 16, 0))
                .status(null)
                .buildMeetingRoom());

        when(mapper.select(page.getPageSize(), page.getOffset(),
                criteria.getRoomId(), criteria.getRoomName(), criteria.getLocation(),
                criteria.getCapacity(), criteria.getUsername(),
                criteria.getStartTime(), criteria.getEndTime(),
                criteria.getStatus())).thenReturn(mockRooms);

        // Execute test
        List<MeetingRoomDTO.Resp> result = service.list(page, criteria);

        // Assertions
        assertEquals(2, result.size(), "Expected 2 meeting rooms in result.");
        assertEquals("Room A", result.get(0).getRoomName());
        assertEquals("Room B", result.get(1).getRoomName());
        verify(mapper, times(1)).select(anyInt(), anyInt(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testListWithException() {
        Page page = new Page();
        page.setPageNo(1);
        page.setPageSize(2);

        MeetingRoomCriteria criteria = new MeetingRoomCriteria();
        when(mapper.select(anyInt(), anyInt(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Database error"));

        // Execute and assert exception
        Exception exception = assertThrows(RuntimeException.class, () -> service.list(page, criteria));
        assertEquals("Could not fetch meeting room list, please try again later.", exception.getMessage());
        verify(mapper, times(1)).select(anyInt(), anyInt(), any(), any(), any(), any(), any(), any(), any(), any());
    }
}
