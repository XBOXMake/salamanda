package moe.salamanda.salamanda.models.student;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.general.WithUser;
import moe.salamanda.salamanda.models.blog.BlogComment;
import moe.salamanda.salamanda.models.blog.BlogContent;
import moe.salamanda.salamanda.models.blog.BlogLike;
import moe.salamanda.salamanda.models.course.CourseGrade;

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
public class Student extends WithUser implements Serializable {
    public Student(WithUser user){
        super(user);
    }

    private String studentID;
    private String withGrade;//专业
    private int withClass;//班级
    private String classID;
    private File image;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @OneToMany(mappedBy = "from",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> commentsA;//评论了谁-学生互评

    @OneToMany(mappedBy = "to",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> commentsB;//被谁评论-学生互评

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BlogContent> blogs;//博客

    @OneToMany(mappedBy = "by",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BlogComment> blogComments;//博客评论

    @OneToMany(mappedBy = "by",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BlogLike> blogLikes;//博客点赞

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CourseGrade> courseGrades;//课程选择/成绩

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Activity> activities;//社会实践
}