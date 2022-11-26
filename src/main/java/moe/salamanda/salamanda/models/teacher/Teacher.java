package moe.salamanda.salamanda.models.teacher;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.course.Course;
import moe.salamanda.salamanda.models.general.WithUser;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.List;

//老师
@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "teacher")
public class Teacher extends WithUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teacherID;
    @Length(max = 100)
    private String researchDirect;
    @Length(max = 1000)
    private String papers;
    private File image;

    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Course> courses;//课程
}
