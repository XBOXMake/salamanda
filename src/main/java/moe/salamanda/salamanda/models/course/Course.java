package moe.salamanda.salamanda.models.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.teacher.Teacher;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

//课程
@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "course")
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CourseGrade> courseGrades;//课程成绩

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dateStart;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dateEnd;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date schedule;
    private int limitation;//人数限制
    @NotEmpty
    private int grade;//学分

    @NotBlank
    @Length(max = 10)
    private String name;
    @NotBlank
    @Length(max = 100)
    private String description;

    @ManyToOne(targetEntity = Teacher.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Teacher teacher;

}
