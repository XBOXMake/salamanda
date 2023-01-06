package moe.salamanda.salamanda.repositories.teacher;

import moe.salamanda.salamanda.models.teacher.CourseGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseGradeRepository extends JpaRepository<CourseGrade,Long> {

    @Query(value = "select * from grade where grade_from_id = :ida and grade_to_id = :idb",nativeQuery = true)
    CourseGrade findByCourseAndStudent(@Param("ida") Long studentID,@Param("idb") Long courseID);

    @Query(value = "select * from grade where id = :number",nativeQuery = true)
    CourseGrade findById(@Param("number")Integer number);
}
