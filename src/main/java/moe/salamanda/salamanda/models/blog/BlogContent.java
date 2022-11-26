package moe.salamanda.salamanda.models.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.student.Student;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

//博客
@Data
@Entity
@NoArgsConstructor
@Table(name = "blog")
@Accessors(chain = true)
public class BlogContent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Student.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_id")
    @JsonBackReference
    private Student author;

    @Temporal(TemporalType.DATE)
    private Date date;

    @NotBlank
    @Length(max = 300000)
    private String content;

    private List<File> files;

    @OneToMany(mappedBy = "blog",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BlogComment> comments;

    @OneToMany(mappedBy = "blog",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BlogLike> likes;
}
