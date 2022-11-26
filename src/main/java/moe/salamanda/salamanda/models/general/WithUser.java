package moe.salamanda.salamanda.models.general;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "user")
public class WithUser implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 30)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Length(min = 10)
    private String password;

    @Length(max = 200)
    private String introductions;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Length(max = 11,min = 11)
    private String phone;
    //basic part

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Max(2)//female
    @Min(1)//male
    private Integer sex;
    private File thumbnail;
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
    public static Integer getRoleAttribute(String role){
        switch (role){
            case "STUDENT": return 1;
            case "TEACHER": return 2;
            case "ADMIN": return 3;
            default: return 0;
        }
    }
    //identity part
}
