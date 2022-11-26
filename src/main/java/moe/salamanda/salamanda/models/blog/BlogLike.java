package moe.salamanda.salamanda.models.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.student.Student;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//博客点赞
@Data
@Entity
@NoArgsConstructor
@Table(name = "blog_like")
@Accessors(chain = true)
public class BlogLike implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = BlogContent.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_like_id")
    @JsonBackReference
    private BlogContent blog;

    @ManyToOne(targetEntity = Student.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_like_id")
    @JsonBackReference
    private Student by;

    @Temporal(TemporalType.DATE)
    private Date date;
}
