package moe.salamanda.salamanda.models.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    enum Tag{
        学霸,笔记大王,自习室常客,实验室常客,图书馆常客,竞赛选手,竞赛狂魔,ddl战士,富哥,富婆,校草,校花,运动健将,健身达人,神行太保,足球小将,篮球高手,排球达人,乒乓小将,羽毛球达人,网球高手,养身人,修仙人,夜猫子,游戏爱好者,游戏高手,数码迷,摄影师,音乐人,K歌人,干饭人,食堂常客,外卖常客,快递常客,文学青年,写手,小说家,交友达人,打工战士,志愿者战士,热心的同学
    }

    private static String TagRev[] ={
            "学霸","笔记大王","自习室常客","实验室常客","图书馆常客","竞赛选手","竞赛狂魔","ddl战士","富哥","富婆","校草","校花","运动健将","健身达人","神行太保","足球小将","篮球高手","排球达人","乒乓小将","羽毛球达人","网球高手","养身人","修仙人","夜猫子","游戏爱好者","游戏高手","数码迷","摄影师","音乐人","K歌人","干饭人","食堂常客","外卖常客","快递常客","文学青年","写手","小说家","交友达人","打工战士","志愿者战士","热心的同学"
    };
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Student.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_to_id")
    @JsonBackReference
    private Student to;

    @Max(40)
    @Min(0)
    private Integer content;

    public static String getTag(Integer number){
        return TagRev[number];
    }
}
