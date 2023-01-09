package moe.salamanda.salamanda.models.general;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.salamanda.salamanda.models.student.Student;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "class")
public class WithClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(9999)
    @Min(1)
    private Integer year;
    @Max(99)
    private Integer withClass;

    @ManyToOne(targetEntity = WithSubject.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    @JsonBackReference
    private WithSubject withSubject;

    public static String total(WithClass withClass){
        try{
            return withClass.getYear().toString() + "级" + withClass.getWithSubject().getName() + withClass.getWithClass().toString() + "班";
        }
        catch (Exception e){
            return "";
        }
    }

    @OneToMany(mappedBy = "withClass",cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY)
    private List<Student> studentList;
}
