package com.example.hrms.biz.department.service;

import com.example.hrms.biz.department.model.Department;
import com.example.hrms.biz.department.model.criteria.DepartmentCriteria;
import com.example.hrms.biz.department.model.dto.DepartmentDTO;
import com.example.hrms.biz.department.repository.DepartmentMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    public List<Department> findById(Long id) {
        List<Department> departments = departmentMapper.findById(id);
        if (departments.isEmpty()) {
            System.out.println("Department not found: " + id);
        }
        return departments;
    }

    public void insert(DepartmentDTO.Req req) {
        // Kiểm tra trùng tên phòng ban
        if (departmentMapper.countByName(req.getDepartmentName()) > 0) {
            throw new RuntimeException("Department name already exists");
        }
        // Kiểm tra trùng tên nhân viên trong phòng ban
        if (departmentMapper.countUserInDepartment(req.getUserName(), req.getDepartmentId()) > 0) {
            throw new RuntimeException("Username already exists in this department");
        }
        Department department = req.toDepartment();
        departmentMapper.insertDepartment(department);
        // Nếu có username và roleName thì thêm User vào department
        if (req.getUserName() != null && req.getRoleName() != null) {
            departmentMapper.insertUserForDepartment(req.getUserName(), req.getRoleName(), department.getDepartmentId());
        }
    }

    public void updateDepartment(Long id, DepartmentDTO.Req req) {
        if (findById(id).isEmpty()) {
            throw new RuntimeException("Department not found");
        }
        // Kiểm tra tên phòng ban có trùng nhưng loại trừ chính nó
        if (departmentMapper.countByNameExcludingId(req.getDepartmentName(), id) > 0) {
            throw new RuntimeException("Department name already exists");
        }
        Department department = req.toDepartment();
        department.setDepartmentId(id);
        departmentMapper.updateDepartment(department);
        // Update user information if provided
        if (req.getUserName() != null && req.getRoleName() != null) {
            if (departmentMapper.countUserInDepartment(req.getUserName(), id) > 0) {
                departmentMapper.updateUserForDepartment(req.getUserName(), req.getRoleName(), id);
            } else {
                departmentMapper.insertUserForDepartment(req.getUserName(), req.getRoleName(), id);
            }
        }
    }

    public void deleteDepartment(Long departmentId) {
        if (findById(departmentId).isEmpty()) {
            throw new RuntimeException("Department not found");
        }
        departmentMapper.deleteUsersByDepartmentId(departmentId);
        departmentMapper.deleteDepartment(departmentId);
    }

    public int count(DepartmentCriteria criteria) {
        return departmentMapper.count(criteria);
    }

    public List<DepartmentDTO.Resp> list(DepartmentCriteria criteria) {
        List<Department> departments = departmentMapper.filterByCriteria(criteria);
        return departments.stream().map(DepartmentDTO.Resp::toResponse).toList();
    }

    public List<Department> getAllDepartments() {
        return departmentMapper.filterByCriteria(new DepartmentCriteria());
    }
}