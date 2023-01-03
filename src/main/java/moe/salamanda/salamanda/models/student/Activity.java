package moe.salamanda.salamanda.models.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

//所有的学生额外项
@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "activity")
public class Activity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Student.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    @JsonBackReference
    private Student student;

    @Min(1)
    @Max(3)
    private int attribute;
    //1 社会实践 2 课外活动 3 成果奖励

    @NotBlank
    @Length(max = 20)
    private String name;
    @NotBlank
    @Length(max = 300)
    private String contentA;//内容
    @NotBlank
    @Length(max = 300)
    private String contentB;//成果
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dateStart;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    public String getRole(){
        switch (attribute){
            case 1:return "achievement";
            case 2:return "activity";
            case 3:return "practice";
            default:return null;
        }
    }

    public static Integer getAttribute(String role){
        switch (role){
            case "achievement":return 1;
            case "activity":return 2;
            case "practice":return 3;
            default:return 0;
        }

    }
}
