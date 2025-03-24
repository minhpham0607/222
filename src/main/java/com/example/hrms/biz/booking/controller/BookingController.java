package com.example.hrms.biz.booking.controller;

import com.example.hrms.biz.booking.model.Booking;
import com.example.hrms.biz.booking.model.dto.BookingDTO;
import com.example.hrms.biz.booking.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @RequestMapping("")
    public String openBookingView(Model model) {
        model.addAttribute("booking", new Booking());
        return "bookings";
    }

    @GetMapping("/view")
    public String openViewRoom(Model model) {
        List<BookingDTO.Resp> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "viewroom"; //danh sách dữ liệu
    }

    @GetMapping("/edit")
    public String openEditBookingView(@RequestParam("bookingId") Long bookingId, Model model) {
        Booking booking = bookingService.getBookingById(bookingId);
        model.addAttribute("booking", booking);
        return "bookingedit"; // Form chỉnh sửa
    }

    @GetMapping("/delete")
    public String openDeletePopup(@RequestParam("bookingId") Long bookingId, Model model) {
        model.addAttribute("bookingId", bookingId);
        return "popup"; // Form thông báo xóa
    }
}