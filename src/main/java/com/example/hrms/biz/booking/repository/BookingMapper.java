package com.example.hrms.biz.booking.repository;

import com.example.hrms.biz.booking.model.Booking;
import com.example.hrms.biz.booking.model.criteria.BookingCriteria;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BookingMapper {

    @Insert("INSERT INTO bookings (booking_id, username, room_id, start_time, end_time, status, title, attendees) VALUES (#{bookingId}, #{username}, #{roomId}, #{startTime}, #{endTime}, #{status}, #{title}, #{attendees})")
    void insert(Booking booking);

    @Select("SELECT booking_id, username, room_id, start_time, end_time, status, title, attendees FROM bookings WHERE booking_id = #{bookingId}")
    @Results({
            @Result(property = "bookingId", column = "booking_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "roomId", column = "room_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "title", column = "title"),
            @Result(property = "attendees", column = "attendees")
    })
    Booking selectById(@Param("bookingId") long bookingId);

    int count(BookingCriteria criteria);

    List<Booking> select(BookingCriteria criteria);

    @Select("SELECT booking_id, username, room_id, start_time, end_time, status, title, attendees FROM bookings WHERE room_id = #{roomId} AND ((start_time < #{endTime} AND end_time > #{startTime}))")
    @Results({
            @Result(property = "bookingId", column = "booking_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "roomId", column = "room_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "title", column = "title"),
            @Result(property = "attendees", column = "attendees")
    })
    List<Booking> findConflictingBookings(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Update("UPDATE bookings SET username = #{username}, room_id = #{roomId}, start_time = #{startTime}, end_time = #{endTime}, status = #{status}, title = #{title}, attendees = #{attendees} WHERE booking_id = #{bookingId}")
    void updateBooking(Booking booking);

    @Delete("DELETE FROM bookings WHERE booking_id = #{bookingId}")
    void deleteBooking(@Param("bookingId") Long bookingId);

    // Thêm phương thức này để lấy tất cả các đặt phòng
    @Select("SELECT booking_id, username, room_id, start_time, end_time, status, title, attendees FROM bookings")
    @Results({
            @Result(property = "bookingId", column = "booking_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "roomId", column = "room_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "title", column = "title"),
            @Result(property = "attendees", column = "attendees")
    })
    List<Booking> selectAll();
}