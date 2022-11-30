package moe.salamanda.salamanda.models.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.student.Student;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

//博客评论
@Data
@Entity
@NoArgsConstructor
@Table(name = "blog_comment")
@Accessors(chain = true)
public class BlogComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = BlogContent.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_comment_to_id")
    @JsonBackReference
    private BlogContent blog;

    @ManyToOne(targetEntity = Student.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_comment_from_id")
    @JsonBackReference
    private Student by;

    @Temporal(TemporalType.DATE)
    private Date date;

    @NotBlank
    @Length(max = 300)
    private String content;

}
