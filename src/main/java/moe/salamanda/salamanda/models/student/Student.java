package moe.salamanda.salamanda.models.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.general.WithClass;
import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.models.teacher.CourseGrade;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

//学生;
@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "Student")
public class Student extends WithUser implements Serializable {
    public Student(WithUser user){
        super(user);
    }

    private String studentID;
    private File image;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @ManyToOne(targetEntity = WithClass.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    @JsonBackReference
    private WithClass withClass;

    @OneToMany(mappedBy = "from",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> commentsA;//评论了谁-学生互评

    @OneToMany(mappedBy = "to",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> commentsB;//被谁评论-学生互评

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Blog> blogs;//博客

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CourseGrade> courseGrades;//课程选择/成绩

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Activity> activities;//社会实践
}