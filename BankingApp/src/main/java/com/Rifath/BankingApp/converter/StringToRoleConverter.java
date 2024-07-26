package com.Rifath.BankingApp.converter;

import com.Rifath.BankingApp.dao.RoleRepository;
import com.Rifath.BankingApp.entity.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    private final RoleRepository roleRepository;

    public StringToRoleConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }



    @Override
    public Role convert(String source) {
        return roleRepository.findByName(source);
    }
}

