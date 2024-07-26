package com.Rifath.BankingApp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private StringToRoleConverter stringToRoleConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToRoleConverter);
    }

    private final LoanStatusConverter loanStatusConverter;

    public WebConfig(LoanStatusConverter loanStatusConverter) {
        this.loanStatusConverter = loanStatusConverter;
    }



}
