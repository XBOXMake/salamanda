package moe.salamanda.salamanda.models.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.general.WithClass;
import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.models.teacher.Course;
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

    private File image;

    public static String getStudentID(Student student){
        try{
            String ID = student.getWithClass().getYear().toString();
            while (ID.length() < 4) ID = ID + '0';
            String temp = student.getWithClass().getWithSubject().getId().toString();
            while (temp.length() < 3) temp = '0' + temp;
            ID = ID + temp;
            temp = student.getWithClass().getWithClass().toString();
            while (temp.length() < 2) temp = '0' + temp;
            ID = ID + temp;
            temp = Integer.toString(student.getWithClass().getStudentList().indexOf(student) + 1);
            while (temp.length() < 3) temp = '0' + temp;
            ID = ID + temp;
            return ID;
        }
        catch (Exception e){
            return "";
        }
    }

    public boolean courseOverflow(Course course){
        List<CourseGrade> gradeList = this.getCourseGrades();
        boolean ans = false;
        for(CourseGrade grade:gradeList){
            Course temp = grade.getCourse();
            if(course.getId() == temp.getId()) return false;
            if(Course.isDateOverflow(temp,course)&&Course.isScheduleOverflow(temp,course)){
                ans = true;
            }
        }
        return ans;

    }

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @ManyToOne(targetEntity = WithClass.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    @JsonBackReference
    private WithClass withClass;

    @OneToMany(mappedBy = "author",cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<Blog> blogs;//博客

    @OneToMany(mappedBy = "student",cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<CourseGrade> courseGrades;//课程选择/成绩

    @OneToMany(mappedBy = "student",cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<Activity> activities;//社会实践

    @OneToMany(mappedBy = "to",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments;//被谁评论-学生互评
}