package moe.salamanda.salamanda.services;

import moe.salamanda.salamanda.models.Users;
import moe.salamanda.salamanda.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service("userDetailService")
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Users user = userRepository.findByUsername(username);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("不存在拥有该用户名的账号！");
        }
        List<GrantedAuthority> authList = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());
        return new User(user.getUsername(),new BCryptPasswordEncoder().encode(user.getUsername()),authList);
    }
}
