package moe.salamanda.salamanda.repositories.student;

import moe.salamanda.salamanda.models.student.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {

    @Query(value = "Select * from blog order by date Desc limit 1 offset :number",nativeQuery = true)
    Blog getBlogByTime(@Param("number") int sortNumber);
}
