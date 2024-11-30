package org.example.sd_94vs1.repository;

import org.example.sd_94vs1.entity.warranty.WarrantyClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarrantyClaimRepo extends JpaRepository<WarrantyClaim,String> {
    WarrantyClaim findWarrantyClaimByClaimCode(String claimCode);
}
