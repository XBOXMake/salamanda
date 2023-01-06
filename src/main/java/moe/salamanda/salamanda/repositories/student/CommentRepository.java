package moe.salamanda.salamanda.repositories.student;

import moe.salamanda.salamanda.models.student.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
