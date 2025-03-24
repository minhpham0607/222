package com.example.hrms.biz.department.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @GetMapping("")
    public String openDepartmentView(Model model) {
        return "department"; // Ensure this matches the name of your HTML file
    }
}