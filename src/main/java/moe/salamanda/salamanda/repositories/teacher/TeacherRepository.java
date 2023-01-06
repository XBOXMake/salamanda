package moe.salamanda.salamanda.repositories.teacher;

import moe.salamanda.salamanda.models.teacher.Course;
import moe.salamanda.salamanda.models.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    @Query(value = "select * form user where attribute = 2",nativeQuery = true)
    ArrayList<Teacher> findAll();

    @Query(value = "Select * from user where attribute= 2 and LOWER(username)= :name",nativeQuery = true)
    Teacher findByUsername(@Param("name") String username);
}
