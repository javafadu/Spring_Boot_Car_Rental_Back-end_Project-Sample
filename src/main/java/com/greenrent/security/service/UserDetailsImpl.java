package com.greenrent.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenrent.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {


    /**
     * Spring security does not know my User entity class.
     * It considers UserDetails interface imported with spring security. So we need a class to Implement
     * user information from UserDetails class and mix with our User information.
     */

    private static final long serialVersionUID = 1L;

    private Long id;

    private String email;

    @JsonIgnore // bu bilgiyi disarida kullanilmasin, ignore etsin cekerken
    private String password;

    private Collection<? extends GrantedAuthority> authorities;


    // Her bir rol bilgimi GrantedAuthority e cevirmek :
    // Cunku spring security bir framework, catiyi o kuruyor biz de onun
    // istedigi tipten veriler dondurmemiz gerekiyor.
    /*
    Kendi db deki User entity lerden UserDetails ler olusturacagim.

    UserDetailsImp class imiz UserDetails den implemente edildi.
    bir tane build diye bir method oluşturcağım, içine bir user alacağım,
    her gelen user imin öncelikle rollerini alacağım, onları strem e alarak
    bu gelen her bir rolu simple GrantedAuthoriy e map edeceğim, Çünkü spring security benden
    grantedAuthority tipinde yapılar istiyor.

    her rolumu new SimpleGrantedAuthority şeklinde oluşturacağım ve içine sadece rolümün ismini
    role.getName() -> enumaration in kendisini veriyor, role.getName().name() String olan name i veriyor.
    sonra aldığım rolü Collection list e çeviyoruz.

    Oluşan liste GrantedAuthority tipinden bir liste

     */


    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        // UserDetailsImp Constructor -> @AllArgsConstructor
        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
