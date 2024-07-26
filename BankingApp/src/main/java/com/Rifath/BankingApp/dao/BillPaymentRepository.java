package com.Rifath.BankingApp.dao;

import com.Rifath.BankingApp.entity.BillPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillPaymentRepository extends JpaRepository<BillPayment,Long> {
    List<BillPayment> findByAccount_User_Username(String username);

}
