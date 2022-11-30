package moe.salamanda.salamanda.services;

import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.models.student.Student;
import moe.salamanda.salamanda.models.teacher.Teacher;
import moe.salamanda.salamanda.repositories.StudentRepository;
import moe.salamanda.salamanda.repositories.TeacherRepository;
import moe.salamanda.salamanda.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service("userDetailService")
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    public void insertUser(WithUser user){
        userRepository.save(user);
    }
    public void insertTeacher(Teacher user){
        teacherRepository.save(user);
    }
    public void insertStudent(Student user){
        studentRepository.save(user);
    }
    public WithUser getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public WithUser getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public void resetPassword(String email,String password){
        WithUser user = userRepository.findByEmail(email);
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        WithUser user = userRepository.findByUsername(username);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("不存在拥有该用户名的账号！");
        }
        List<GrantedAuthority> authList = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());
        return new User(user.getUsername(),PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword()),authList);
    }

    public UserDetails loadUserByUsernameAndAttribute(String username,Integer attribute) throws UsernameNotFoundException{
        WithUser user = userRepository.findByUsernameAndAttribute(username,attribute);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("不存在拥有该用户名的账号！");
        }
        List<GrantedAuthority> authList = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());
        return new User(user.getUsername(), PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword()),authList);
    }
}
