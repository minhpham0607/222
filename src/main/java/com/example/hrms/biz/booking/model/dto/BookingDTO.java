package com.example.hrms.biz.booking.model.dto;

import com.example.hrms.biz.booking.model.Booking;
import com.example.hrms.enumation.BookingStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import com.example.hrms.utils.DateUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingDTO {

    @Getter
    @Setter
    public static class Req {
        private Long bookingId;
        private String username;
        private Long roomId;
        private String startTime;
        private String endTime;
        private String status;
        private String title; // Thêm trường title
        private String attendees; // Thêm trường attendees

        public Booking toBooking() {
            Booking booking = new Booking();
            BeanUtils.copyProperties(this, booking);
            DateTimeFormatter formatter = DateUtils.getDateTimeFormatter();
            booking.setStartTime(LocalDateTime.parse(this.startTime, formatter));
            booking.setEndTime(LocalDateTime.parse(this.endTime, formatter));
            booking.setStatus(BookingStatusEnum.valueOf(this.status));
            booking.setTitle(this.title);
            booking.setAttendees(this.attendees);
            return booking;
        }
    }

    @Getter
    @Setter
    public static class Resp {
        private Long bookingId;
        private String username;
        private Long roomId;
        private String startTime;
        private String endTime;
        private String status;
        private String title; // Thêm trường title
        private String attendees; // Thêm trường attendees

        public static Resp toResponse(Booking booking) {
            Resp resp = new Resp();
            BeanUtils.copyProperties(booking, resp);
            DateTimeFormatter formatter = DateUtils.getDateTimeFormatter();
            resp.setStartTime(booking.getStartTime().format(formatter));
            resp.setEndTime(booking.getEndTime().format(formatter));
            resp.setStatus(booking.getStatus().name());
            resp.setTitle(booking.getTitle()); // Sao chép title
            resp.setAttendees(booking.getAttendees()); // Sao chép attendees
            return resp;
        }
    }
}