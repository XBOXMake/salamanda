package moe.salamanda.salamanda.models.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.student.Student;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

//博客
@Data
@Entity
@NoArgsConstructor
@Table(name = "blog")
@Accessors(chain = true)
public class Blog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference
    private Student author;

    @Temporal(TemporalType.DATE)
    private Date date;

    @NotBlank
    private String content;
}
