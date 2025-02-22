package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BillRepo extends JpaRepository<Bill, Long>{

    @Modifying
    @Transactional
    @Query("UPDATE Bill b SET b.bill_status = 'Complete' WHERE b.bill_id = :billId")
    void updateBillStatus(Long billId);
}
