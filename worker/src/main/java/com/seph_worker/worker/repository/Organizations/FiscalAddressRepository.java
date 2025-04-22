package com.seph_worker.worker.repository.Organizations;


import com.seph_worker.worker.core.entity.organizations.FiscalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiscalAddressRepository extends JpaRepository<FiscalAddress, Integer> {

}
