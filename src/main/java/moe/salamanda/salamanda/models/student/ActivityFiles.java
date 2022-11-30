package moe.salamanda.salamanda.models.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.File;

@Data
@Entity
@NoArgsConstructor
@Table(name = "activity_file")
@Accessors(chain = true)
public class ActivityFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Activity.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_file_id")
    @JsonBackReference
    private Activity activity;

    private String filename;
    private File file;
}
