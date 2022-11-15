package moe.salamanda.salamanda.repositories;

import moe.salamanda.salamanda.models.WithUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends JpaRepository<WithUser,Long> {
    @Query(value = "select * form user",nativeQuery = true)
    ArrayList<WithUser> findAll();

    @Query(value = "Select * from user where username= :name",nativeQuery = true)
    WithUser findByUsername(@Param("name") String username);

    @Query(value = "Select * from user where (username= :name and attribute= :attribute)",nativeQuery = true)
    WithUser findByUsernameAndAttribute(@Param("name") String username,@Param("attribute") Integer attribute);
}
