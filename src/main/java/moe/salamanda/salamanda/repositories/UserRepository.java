package moe.salamanda.salamanda.repositories;

import moe.salamanda.salamanda.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    @Query(value = "select * form users",nativeQuery = true)
    public ArrayList<Users> findAll();

    @Query(value = "Select * from users where username='%:name%'",nativeQuery = true)
    public Users findByUsername(@Param("name") String username);

    @Query(value = "Select * from users where (username='%:name%' and attribute='%attribute%')",nativeQuery = true)
    public Users findByUsernameAndAttribute(@Param("name") String username,@Param("attribute") Integer attribute);
}
