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
    @JoinColumn(name = "activity_id")
    @JsonBackReference
    private Student student;

    @Min(1)
    @Max(3)
    private int attribute;
    //1 社会实践 2 课外活动 3 成果奖励

    @NotBlank
    @Length(max = 10)
    private String name;
    @NotBlank
    @Length(max = 100)
    private String contentA;//内容
    @NotBlank
    @Length(max = 100)
    private String contentB;//成果
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dateStart;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dateEnd;
}
