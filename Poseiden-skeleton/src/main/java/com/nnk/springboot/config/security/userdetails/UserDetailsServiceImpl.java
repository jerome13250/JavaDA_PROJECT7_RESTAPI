package com.nnk.springboot.config.security.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Custom UserDetailsService implementation for Spring Security authentication.
 * 
 * <p>
 * Implements UserDetailsService interface that is used to retrieve user-related data.
 * The method loadUserByUsername() is overridden to customize the process of finding the user.
 * </p>
 * 
 * @author jerome
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
	@Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}