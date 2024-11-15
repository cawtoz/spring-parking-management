package com.github.cawtoz.parking.model.security;

import com.github.cawtoz.parking.model.Parking;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final String email;
    private final String name;
    private final String surname;
    private final String phone;
    private final Parking parking;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phone = user.getPhone();
        System.out.println(user.getParking());
        this.parking = user.getParking();
        this.authorities = AuthorityUtils.createAuthorityList(user.getRole().name());
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
