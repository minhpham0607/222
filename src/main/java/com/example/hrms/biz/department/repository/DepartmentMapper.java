package com.example.hrms.biz.department.repository;

import com.example.hrms.biz.department.model.Department;
import com.example.hrms.biz.department.model.criteria.DepartmentCriteria;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DepartmentMapper {

    @Insert("INSERT INTO Departments (department_name) VALUES (#{departmentName})")
    @Options(useGeneratedKeys = true, keyProperty = "departmentId")
    void insertDepartment(Department department);

    @Select("""
    SELECT COUNT(*) FROM Users WHERE username = #{userName} AND department_id = #{departmentId}
""")
    int countUserInDepartment(@Param("userName") String userName, @Param("departmentId") Long departmentId);

    @Insert("""
    INSERT INTO Users (username, password, department_id, role_name, is_supervisor, status) 
    VALUES (#{userName}, 'default_password', #{departmentId}, #{roleName}, false, 'ACTIVE');
""")
    void insertUserForDepartment(@Param("userName") String userName,
                                 @Param("roleName") String roleName,
                                 @Param("departmentId") Long departmentId);


    @Select("""
    SELECT d.department_id AS departmentId,
           d.department_name AS departmentName,
           r.role_name AS roleName,
           u.username AS userName
    FROM Departments d
    JOIN Users u ON d.department_id = u.department_id
    JOIN Roles r ON u.role_name = r.role_name
    WHERE d.department_id = #{id}
    ORDER BY r.role_name ASC;
""")
    List<Department> findById(Long id);

    @Select("""
    <script>
        SELECT d.department_id AS departmentId,
               d.department_name AS departmentName,
               u.username AS userName,
               r.role_name AS roleName
        FROM Users u
        JOIN Departments d ON u.department_id = d.department_id
        JOIN Roles r ON u.role_name = r.role_name
        WHERE 1=1
        <if test='departmentId != null'>
            AND d.department_id = #{departmentId}
        </if>
        <if test='departmentName != null'>
            AND d.department_name LIKE CONCAT('%', #{departmentName}, '%')
        </if>
        <if test='userName != null'>
            AND u.username LIKE CONCAT('%', #{userName}, '%')
        </if>
        <if test='roleName != null'>
            AND r.role_name LIKE CONCAT('%', #{roleName}, '%')
        </if>
        ORDER BY d.department_name ASC, r.role_name ASC;
        
    </script>
""")
    List<Department> filterByCriteria(DepartmentCriteria criteria);

    @Select("""
    SELECT COUNT(*) 
    FROM Departments 
    WHERE department_name = #{departmentName} AND department_id != #{departmentId}
""")
    int countByNameExcludingId(@Param("departmentName") String departmentName, @Param("departmentId") Long departmentId);


    @Select("SELECT COUNT(*) FROM Departments WHERE department_name = #{departmentName}")
    int countByName(String departmentName);

    @Select("SELECT COUNT(*) FROM Departments")
    int count(DepartmentCriteria criteria);

    @Update("""
    UPDATE Users
    SET password = 'default_password', department_id = #{departmentId}, role_name = #{roleName}, is_supervisor = false, status = 'ACTIVE'
    WHERE username = #{userName};
""")
    void updateUserForDepartment(@Param("userName") String userName, @Param("roleName") String roleName, @Param("departmentId") Long departmentId);
    @Update("UPDATE Departments SET department_name = #{departmentName} WHERE department_id = #{departmentId} ")
    void updateDepartment(Department department);

    @Delete("DELETE FROM Users WHERE department_id = #{departmentId}")
    void deleteUsersByDepartmentId(Long departmentId);

    @Delete("DELETE FROM Departments WHERE department_id = #{departmentId}")
    void deleteDepartment(Long departmentId);
}