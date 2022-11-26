package moe.salamanda.salamanda.models.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

//学生互评
@Data
@Entity
@NoArgsConstructor
@Table(name = "comment")
@Accessors(chain = true)
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = Student.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id")
    @JsonBackReference
    private Student from;

    @ManyToOne(targetEntity = Student.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id")
    @JsonBackReference
    private Student to;

    @NotBlank
    @Length(max = 300)
    private String content;

    @Temporal(TemporalType.DATE)
    private Date date;
}
