package moe.salamanda.salamanda.models;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class Users implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 30)
    private String username;

    @NotBlank
    @Length(min = 10)
    private String password;

    @Length(max = 200)
    private String introductions;

    @NotBlank
    @Email
    private String email;
    //basic part

    private String firstName;
    private String lastName;
    private String thumbnail_url;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Max(2)//female
    @Min(1)//male
    private Integer sex;
    //information part

    @Max(3)
    @Min(1)
    //1 student 2 teacher 3 admin
    private Integer Attribute;
    public String getRole(){
        switch (Attribute){
            case 1: return "STUDENT";
            case 2: return "TEACHER";
            case 3: return "ADMIN";
            default: return null;
        }
    }
    //identity part
}
