package com.seph_worker.worker.repository.UserRoleModule;

import com.seph_worker.worker.core.entity.RoleModuleUser.Modules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface ModuleRepository extends JpaRepository<Modules, Integer> {

    @Query(value = """
SELECT
m.name AS name,
m.id AS id,
i.icon,
m.description,
m1.name AS parent,
m.visible AS vista
FROM modules m
LEFT JOIN modules m1 ON m1.id = m.parent_id
LEFT JOIN icons i ON i.id = m.icon_id
WHERE m.deleted = 0 
""", nativeQuery = true)
    List<Map<String,Object>> getAllModules();

    @Query(value = """
SELECT
m.name AS name,
m.id AS id,
i.icon,
m.description,
m1.name AS parent,
m.visible AS vista
FROM modules m
LEFT JOIN modules m1 ON m1.id = m.parent_id
LEFT JOIN icons i ON i.id = m.icon_id
WHERE m.id=:moduleId
AND m.deleted = 0 
""", nativeQuery = true)
    Map<String,Object> getModuleById(Integer moduleId);

    @Query(value = """
SELECT
r.name,
r.id
FROM  roles_modules rm
INNER JOIN roles r ON rm.role_id = r.id
WHERE rm.module_id=:moduleId
AND rm.deleted = 0 
AND r.deleted = 0 
""", nativeQuery = true)
    List<Map<String,Object>> getRoleByModule(Integer moduleId);

    @Query(value = """
SELECT
pr.id,
pr.editar,
pr.agregar,
pr.eliminar,
pr.auto
FROM permisos_roles pr

""", nativeQuery = true)
    List<Map<String,Object>> getPermissions();


    Modules findByName(String name);
}
