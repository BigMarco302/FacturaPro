package com.seph_worker.worker.repository.Organizations;


import com.seph_worker.worker.core.entity.organizations.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {



    @Query(value = """

    SELECT
*
    FROM owner_organizations ur 
    WHERE ur.deleted = 0 
""", nativeQuery = true)
    List<Map<String,String>> getAll();

    @Query(value = """

    SELECT
*
    FROM boletos ur 
    WHERE ur.boleto =:numero 
    AND ur.deleted = 0 
""", nativeQuery = true)
    Map<String,String> boletos(String numero);
}
