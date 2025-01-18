package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillRepo extends JpaRepository<Bill, Long>{

    //01/19/2025
    @Query("SELECT b.bill_name, s.end_date, s.due_time FROM Bill b JOIN b.schedule s")
    List<Object[]> findBillDetails();
}
