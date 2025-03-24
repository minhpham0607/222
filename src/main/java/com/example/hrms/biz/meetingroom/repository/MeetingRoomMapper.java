package com.example.hrms.biz.meetingroom.repository;

import com.example.hrms.biz.meetingroom.model.MeetingRoom;
import com.example.hrms.biz.meetingroom.model.criteria.MeetingRoomCriteria;
import com.example.hrms.enumation.BookingStatusEnum;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MeetingRoomMapper {

    @Select("SELECT COUNT(room_id) FROM meeting_rooms")
    int count(MeetingRoomCriteria criteria);

    @Select("SELECT " +
            "    m.room_id AS roomId, " +
            "    m.room_name AS roomName, " +
            "    m.location AS location, " +
            "    m.capacity AS capacity, " +
            "    b.username AS username, " +
            "    b.start_time AS startTime, " +
            "    b.end_time AS endTime, " +
            "    b.status AS status " +
            "FROM " +
            "    meeting_rooms m " +
            "LEFT JOIN " +
            "    bookings b ON m.room_id = b.room_id AND b.start_time = ( " +
            "        SELECT MIN(b2.start_time) " +
            "        FROM bookings b2 " +
            "        WHERE b2.room_id = m.room_id " +
            "    ) " +
            "WHERE " +
            "    (#{roomId} IS NULL OR m.room_id = #{roomId}) " +
            "    AND (#{roomName} IS NULL OR m.room_name = #{roomName}) " +
            "    AND (#{location} IS NULL OR m.location = #{location}) " +
            "    AND (#{capacity} IS NULL OR m.capacity >= #{capacity}) " +
            "    AND (#{username} IS NULL OR b.username = #{username}) " +
            "    AND (#{startTime} IS NULL OR b.start_time >= #{startTime}) " +
            "    AND (#{endTime} IS NULL OR b.end_time <= #{endTime}) " +
            "    AND (#{status} IS NULL OR b.status = #{status}) " +
            "ORDER BY " +
            "    m.room_id ASC " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<MeetingRoom> select(@Param("pageSize") int pageSize,
                             @Param("offset") int offset,
                             @Param("roomId") Long roomId,
                             @Param("roomName") String roomName,
                             @Param("location") String location,
                             @Param("capacity") Integer capacity,
                             @Param("username") String username,
                             @Param("startTime") LocalDateTime startTime,
                             @Param("endTime") LocalDateTime endTime,
                             @Param("status") BookingStatusEnum status);
}
