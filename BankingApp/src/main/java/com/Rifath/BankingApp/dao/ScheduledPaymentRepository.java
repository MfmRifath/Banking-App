package com.Rifath.BankingApp.dao;

import com.Rifath.BankingApp.entity.ScheduledPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduledPaymentRepository extends JpaRepository<ScheduledPayment,Long> {
    List<ScheduledPayment> findByAccount_User_Username(String username);

}
