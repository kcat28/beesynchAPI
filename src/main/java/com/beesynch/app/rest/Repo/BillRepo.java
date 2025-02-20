package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillRepo extends JpaRepository<Bill, Long>{

}
