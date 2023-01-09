package moe.salamanda.salamanda.models.teacher;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.thymeleaf.expression.Strings;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @OneToMany(mappedBy = "course",cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<CourseGrade> courseGrades;//课程成绩

    @Temporal(TemporalType.DATE)
    private Date selectDateStart;
    @Temporal(TemporalType.DATE)
    private Date selectDateEnd;
    @Temporal(TemporalType.DATE)
    private Date dateStart;
    @Temporal(TemporalType.DATE)
    private Date dateEnd;
    private String schedule;
    private Integer limitation;//人数限制
    private Integer grade;//学分

    public static String week[]={"","星期一","星期二","星期三","星期四","星期五","星期六","星期日"};

    public static String getSchedule(Course course){
        String string = course.getSchedule();
        String[] strings = string.split(";");
        String ans="";
        boolean check[]={false,false,false,false,false,false,false,false};
        for(int i=1;i< strings.length;i++){
            String[] schedule = strings[i].split("-");
            if(!check[Integer.parseInt(schedule[0])]){
                check[Integer.parseInt(schedule[0])]=true;
                ans = ans+(ans.equals("")?"":"节;")+week[Integer.parseInt(schedule[0])]+":";
            }
            else ans = ans + ",";
            ans = ans + schedule[1];
        }
        return ans+"节";
    }

    public static boolean isDateInside(Date a,Course b){
        return a.compareTo(b.getDateStart())>=0&&a.compareTo(b.getDateEnd())<=0;
    }

    public static boolean isDateOverflow(Course a,Course b){
        return a.getDateEnd().compareTo(b.getDateStart())>=0&&a.getDateStart().compareTo(b.getDateEnd())<=0;
    }

    public static boolean isScheduleOverflow(Course a,Course b){
        String[] stringsA = a.getSchedule().split(";");
        String[] stringsB = b.getSchedule().split(";");
        for(String stringA:stringsA){
            for(String stringB:stringsB){
                if(stringB.equals(stringA)&&!stringB.equals("0-0")) return true;
            }
        }
        return false;
    }

    public static boolean isLimitationOverflow(Course course){
        return course.getCourseGrades().size()>=course.getLimitation();
    }

    @NotBlank
    @Length(max = 10)
    private String name;
    @NotBlank
    @Length(max = 200)
    private String description;

    @Column(name = "lock")
    private boolean lock;

    @ManyToOne(targetEntity = Teacher.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    @JsonBackReference
    private Teacher teacher;
}
