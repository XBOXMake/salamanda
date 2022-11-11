package moe.salamanda.salamanda.repositories;

import moe.salamanda.salamanda.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface userRepository extends JpaRepository<User,Long> {
    @Query(value = "select * form users",nativeQuery = true)
    public ArrayList<User> findAll();

    @Query(value = "Select * from users where username='%:name%'",nativeQuery = true)
    public User findByUsername(@Param("name") String username);
}
