package ododock.webserver.domain.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Entity
@Table(name = "content")
@Getter
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

}
