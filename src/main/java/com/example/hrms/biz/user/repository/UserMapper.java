package com.example.hrms.biz.user.repository;

import com.example.hrms.biz.user.model.User;
import com.example.hrms.biz.user.model.criteria.UserCriteria;
import com.example.hrms.enumation.RoleEnum;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    // Lấy tất cả người dùng
    @Select("SELECT username, email, password, department_id, role_name, is_supervisor, status FROM Users")
    List<User> getAllUsers();

    // Lấy người dùng theo tên người dùng
    @Select("SELECT username, email, password, department_id, role_name, is_supervisor, status FROM Users WHERE username = #{username}")
    User getUserByUsername(String username);

    @Select("SELECT password FROM users WHERE username = #{username}")
    String getPasswordByUsername(String username);

    // Thêm người dùng mới
    @Update("UPDATE Users SET email = #{email}, password = #{password}, department_id = #{departmentId}, role_name = #{role_name}, is_supervisor = #{isSupervisor}, status = #{status} WHERE username = #{username}")
    void insertUser(User user);

    // Cập nhật thông tin người dùng
    @Update("UPDATE Users SET email = #{email}, password = #{password}, department_id = #{departmentId}, role_name = #{role_name}, is_supervisor = #{isSupervisor}, status = #{status} WHERE username = #{username}")
    void updateUser(User user);

    // Xóa người dùng theo tên người dùng
    @Delete("DELETE FROM Users WHERE username = #{username}")
    void deleteUser(String username);

    // Tìm kiếm người dùng dựa trên departmentIds và roles
    @Select("<script>" +
            "SELECT username, email, password, department_id, role, is_supervisor, status FROM Users WHERE 1=1" +
            "<if test='departmentIds != null and departmentIds.size() > 0'>" +
            " AND department_id IN " +
            "<foreach item='departmentId' collection='departmentIds' open='(' separator=',' close=')'>" +
            "#{departmentId}" +
            "</foreach>" +
            "</if>" +
            "<if test='roles != null and roles.size() > 0'>" +
            " AND role IN " +
            "<foreach item='role' collection='roles' open='(' separator=',' close=')'>" +
            "#{role}" +
            "</foreach>" +
            "</if>" +
            "</script>")
    List<User> searchUsers(@Param("departmentIds") List<Long> departmentIds, @Param("roles") List<RoleEnum> roles);

    // Đếm số lượng người dùng dựa trên tiêu chí
    int count(UserCriteria criteria);

    // Lấy đơn vị và quyền của tất cả người dùng
    @Select("SELECT department_id, role_name FROM Users")
    List<User> getDepartmentsAndRoles();
    @Select("SELECT COUNT(*) FROM Users WHERE username = #{username}")
    int checkUsernameExists(String username);
}