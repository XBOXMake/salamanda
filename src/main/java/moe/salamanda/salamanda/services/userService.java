package moe.salamanda.salamanda.services;

import lombok.extern.slf4j.Slf4j;
import moe.salamanda.salamanda.models.User;
import moe.salamanda.salamanda.repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class userService implements UserDetailsService {
    @Autowired
    userRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("不存在拥有该用户名的账号！");
        }

    }
}
