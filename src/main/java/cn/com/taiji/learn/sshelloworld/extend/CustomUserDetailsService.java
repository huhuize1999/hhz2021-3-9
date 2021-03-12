package cn.com.taiji.learn.sshelloworld.extend;

import cn.com.taiji.learn.sshelloworld.dao.UserRepository;
import cn.com.taiji.learn.sshelloworld.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;

public class CustomUserDetailsService implements UserDetailsService {
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = userRepository.findByEmail(email);
        if (!user.getEnabled()) {
            throw new UsernameNotFoundException("用户没有被激活，请查看邮箱激活邮件 ");
        }

        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }
        //TODO 从数据库中查询用户所属的角色Role
        Collection<? extends GrantedAuthority> roles ;
        if (user.getEmail().equals("a")) {
            roles = Arrays.asList(new SimpleGrantedAuthority(ROLE_ADMIN));
        }else{
            roles = getAuthorities(ROLE_USER);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }
}
