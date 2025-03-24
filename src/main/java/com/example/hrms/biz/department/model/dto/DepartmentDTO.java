package com.example.hrms.biz.department.model.dto;

import com.example.hrms.biz.department.model.Department;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

public class DepartmentDTO {
    @Getter
    @Setter
    public static class Req {
        private Long departmentId;
        private String departmentName;
        private String roleName;
        private String userName; // Add this field

        public Department toDepartment() {
            Department department = new Department();
            BeanUtils.copyProperties(this, department);
            return department;
        }
    }

    @Getter
    @Setter
    public static class Resp {
        private Long departmentId;
        private String departmentName;
        private String roleName;
        private String userName; // Add this field

        public static Resp toResponse(Department department) {
            Resp resp = new Resp();
            BeanUtils.copyProperties(department, resp);
            return resp;
        }
    }
}