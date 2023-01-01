package moe.salamanda.salamanda.repositories.general;

import moe.salamanda.salamanda.models.general.WithClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WithClassRepository extends JpaRepository<WithClass,Long> {
    @Query(value = "select * from class where id = :number",nativeQuery = true)
    WithClass findById(@Param("number")Integer number);
}
