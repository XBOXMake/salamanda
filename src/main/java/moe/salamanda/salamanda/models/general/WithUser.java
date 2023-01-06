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
@DiscriminatorValue("Admin")
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

    @Length(max = 11,min = 0)
    private String phone;
    //basic part

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Max(3)//female
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

    public String getSexes(){
        if(sex == null) return "";
        switch (sex){
            case 1: return "男";
            case 2: return "女";
            case 3: return "未知";
            default: return null;
        }
    }

    public static String getRoleName(String attribute){
        switch (attribute){
            case "1": return "student";
            case "2": return "teacher";
            case "3": return "admin";
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

    public static String getName(WithUser user){
        return user.getFirstName()+user.getLastName();
    }

    public WithUser(WithUser user){
        id = user.id;
        username = user.username;
        password = user.password;
        introductions = user.introductions;
        email = user.email;
        phone = user.phone;
        firstName = user.firstName;
        lastName = user.lastName;
        sex = user.sex;
        thumbnail = user.thumbnail;
        Attribute = user.Attribute;
    }
    //identity part
}
