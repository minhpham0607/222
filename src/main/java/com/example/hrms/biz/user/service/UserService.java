package com.example.hrms.biz.user.service;

import com.example.hrms.biz.user.model.User;
import com.example.hrms.biz.user.model.criteria.UserCriteria;
import com.example.hrms.biz.user.model.dto.UserDTO;
import com.example.hrms.enumation.RoleEnum;
import com.example.hrms.biz.user.repository.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public boolean checkUsernamePassword(String username, String rawPassword) {
        String encodedPassword = userMapper.getPasswordByUsername(username);
        if (encodedPassword == null) {
            return false; // Username không tồn tại
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    public List<User> searchUsers(UserCriteria criteria) {
        return userMapper.searchUsers(criteria.getDepartmentIds(), criteria.getRoles());
    }

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(String username) {
        userMapper.deleteUser(username);
    }

    public int count(UserCriteria criteria) {
        return userMapper.count(criteria);
    }

    public List<UserDTO.Resp> list(UserCriteria criteria) {
        List<User> users = searchUsers(criteria);
        return users.stream()
                .map(user -> {
                    UserDTO.Resp resp = new UserDTO.Resp();
                    resp.setUsername(user.getUsername());
                    resp.setDepartmentId(user.getDepartmentId());
                    resp.setRole_name(user.getRole_name());
                    resp.setIsSupervisor(user.isSupervisor());
                    resp.setStatus(user.getStatus());
                    return resp;
                })
                .collect(Collectors.toList());
    }

    // Thêm phương thức để lấy đơn vị và quyền của tất cả người dùng
    public List<UserDTO.DepartmentAndRole> getDepartmentsAndRoles() {
        List<User> users = userMapper.getDepartmentsAndRoles();
        return users.stream()
                .map(user -> {
                    UserDTO.DepartmentAndRole resp = new UserDTO.DepartmentAndRole();
                    resp.setDepartmentId(user.getDepartmentId());
                    resp.setRole_name(user.getRole_name());
                    return resp;
                })
                .collect(Collectors.toList());
    }

    public boolean isUsernameDuplicated(String username) {
        return userMapper.checkUsernameExists(username) > 0;
    }

    public RoleEnum getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // Lấy tên quyền từ danh sách Authorities
        String roleName = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);

        if (roleName == null) {
            return null;
        }

        try {
            // Chuyển đổi thành RoleEnum
            return RoleEnum.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Nếu không khớp với Enum
        }
    }
}
