package moe.salamanda.salamanda.repositories.teacher;

import moe.salamanda.salamanda.models.teacher.CourseGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseGradeRepository extends JpaRepository<CourseGrade,Long> {
}
