package com.servPet.meb.model;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MebDetailsService implements UserDetailsService {

    private final MebRepository mebRepository;

    public MebDetailsService(MebRepository mebRepository) {
        this.mebRepository = mebRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MebVO member = mebRepository.findByMebMail(email)
                .orElseThrow(() -> new UsernameNotFoundException("會員不存在"));

        return User.builder()
                .username(member.getMebMail())
                .password(member.getMebPwd())
             
                .authorities(Collections.emptyList()) // 目前無角色權限控制
                .build();
    }  
}
