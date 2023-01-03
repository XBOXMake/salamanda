package moe.salamanda.salamanda.repositories.student;

import moe.salamanda.salamanda.models.student.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {

    @Query(value = "select * from activity where id = :number",nativeQuery = true)
    Activity findById(@Param("number")Integer number);
}
