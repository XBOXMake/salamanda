package moe.salamanda.salamanda.models.teacher;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "Teacher")
public class Teacher extends WithUser implements Serializable {
    public Teacher(WithUser user){
        super(user);
    }

    @Length(max = 100)
    private String researchDirect;
    @Length(max = 300)
    private String papers;
    private File image;

    @OneToMany(mappedBy = "teacher",cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<Course> courses;//课程
}
