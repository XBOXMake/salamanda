package moe.salamanda.salamanda.repositories.student;

import moe.salamanda.salamanda.models.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    @Query(value = "Select * from user where attribute= 1 and LOWER(username)= :name",nativeQuery = true)
    Student findByUsername(@Param("name") String username);

    @Query(value = "Select * from user where attribute= 1 and id= :id",nativeQuery = true)
    Student findById(@Param("id") Integer id);

    @Query(value = "Select * from user where attribute= 1 order by random() limit 1",nativeQuery = true)
    Student randomPickOne();
}
