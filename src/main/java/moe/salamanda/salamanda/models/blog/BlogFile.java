package moe.salamanda.salamanda.models.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.File;

@Data
@Entity
@NoArgsConstructor
@Table(name = "blog_file")
@Accessors(chain = true)
public class BlogFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = BlogContent.class,cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_file_id")
    @JsonBackReference
    private BlogContent blog;

    private String filename;
    private File file;
}
