package id.codefun.web.administrator.util;

import java.util.Collection;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import id.codefun.web.administrator.model.entity.User;
import lombok.Data;

@Data
public class UserPrinciple implements UserDetails {

    private Long id;
    private String name;
    private String username;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id, String name, 
            String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user) {
        return new UserPrinciple(
            user.getId(),
            user.getFullname(),
            user.getEmail(),
            user.getPassword(),
            null
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o || !getClass().equals(o.getClass()) ) {
            return false;
        }
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }

    public int hashCode() {
        return super.hashCode();
    }
    
}
