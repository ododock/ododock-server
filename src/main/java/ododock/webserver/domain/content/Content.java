package ododock.webserver.domain.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Entity
@Getter
//@MappedSuperclass
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;

    @Column(name = "content_name", nullable = false)
    private String name;

    @Column(name = "content_genre", nullable = false)
    private String genre;

    @Column(name = "content_published_date", nullable = false)
    private LocalDate publishedDate;

}
