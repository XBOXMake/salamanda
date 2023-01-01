package moe.salamanda.salamanda.repositories.teacher;

import moe.salamanda.salamanda.models.teacher.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {

}
