package moe.salamanda.salamanda.repositories.teacher;

import moe.salamanda.salamanda.models.teacher.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    @Query(value = "select * from course where id = :number",nativeQuery = true)
    Course findById(@Param("number")Integer number);
}
