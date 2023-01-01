package moe.salamanda.salamanda.models.general;

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
    @Min(2000)
    private Integer year;
    private String subject;
    private Integer withClass;

    public static String total(WithClass withClass){
        return withClass.getYear().toString()+"级"+withClass.getSubject()+withClass.getWithClass().toString()+"班";
    }

    @OneToMany(mappedBy = "withClass",cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.LAZY)
    private List<Student> studentList;
}
