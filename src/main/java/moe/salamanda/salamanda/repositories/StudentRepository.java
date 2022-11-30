package moe.salamanda.salamanda.repositories;

import moe.salamanda.salamanda.models.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    @Query(value = "select * form user where attribute = 1",nativeQuery = true)
    ArrayList<Student> findAll();
}
