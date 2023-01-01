package moe.salamanda.salamanda.models.teacher;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.student.Student;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

//学生only课程选择及其其他相关信息
@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "grade")
public class CourseGrade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne(targetEntity = Student.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "grade_to_id")
    @JsonBackReference
    private Student student;

    @ManyToOne(targetEntity = Course.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "grade_from_id")
    @JsonBackReference
    private Course course;

    @Max(100)
    @Min(0)
    private int number;
}
