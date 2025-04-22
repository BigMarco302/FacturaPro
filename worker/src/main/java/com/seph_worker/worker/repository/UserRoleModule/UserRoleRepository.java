package com.seph_worker.worker.repository.UserRoleModule;


import com.seph_worker.worker.core.entity.RoleModuleUser.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {


    @Query(value = """

    SELECT
        ur.id,
        ur.role_id
    FROM users_roles ur 
    INNER JOIN roles r ON ur.role_id = r.id
    WHERE ur.user_id=:userId
    AND ur.deleted = 0 
    AND r.deleted = 0 
""", nativeQuery = true)
    List<Map<String,Integer>> getRolesByUser(Integer userId);
}
