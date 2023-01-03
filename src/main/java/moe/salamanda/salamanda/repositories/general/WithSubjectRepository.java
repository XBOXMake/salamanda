package moe.salamanda.salamanda.repositories.general;

import moe.salamanda.salamanda.models.general.WithSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WithSubjectRepository extends JpaRepository<WithSubject,Long> {

    @Query(value = "select * from subject where id = :number",nativeQuery = true)
    WithSubject findById(@Param("number")Integer number);
}
