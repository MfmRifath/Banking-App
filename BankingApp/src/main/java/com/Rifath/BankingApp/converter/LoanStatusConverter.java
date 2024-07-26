package com.Rifath.BankingApp.converter;

import com.Rifath.BankingApp.entity.Loan;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LoanStatusConverter implements Converter<String, Loan.LoanStatus> {
    @Override
    public Loan.LoanStatus convert(String source) {
        try {
            return Loan.LoanStatus.valueOf(source);
        } catch (IllegalArgumentException e) {
            return null; // Handle the case where the conversion fails
        }
    }
}
