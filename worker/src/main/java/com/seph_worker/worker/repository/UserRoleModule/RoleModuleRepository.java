package com.seph_worker.worker.repository.UserRoleModule;


import com.seph_worker.worker.core.entity.RoleModuleUser.RoleModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleModuleRepository extends JpaRepository<RoleModule, Integer> {


    @Query(value = """

    SELECT
        ur.id,
        ur.module_id
    FROM roles_modules ur 
    INNER JOIN modules m ON ur.module_id = m.id
    WHERE ur.role_id=:roleId
    AND ur.deleted = 0 
    AND m.deleted = 0 
""", nativeQuery = true)
    List<Map<String,Integer>> getModulesByRole(Integer roleId);

    @Query(value = """

    SELECT
        ur.id,
        ur.role_id
    FROM roles_modules ur 
    INNER JOIN roles r ON ur.role_id = r.id
    WHERE ur.module_id=:moduleId
    AND ur.deleted = 0 
    AND r.deleted = 0 
""", nativeQuery = true)
    List<Map<String,Integer>> getRolesByModule(Integer moduleId);
}
