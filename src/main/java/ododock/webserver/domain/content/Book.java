package ododock.webserver.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.print.attribute.standard.Media;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "book")
@DiscriminatorValue("book")
@NoArgsConstructor
public class Book extends MediaContent {

    @Column(name = "isbn", updatable = false)
    private Long isbn;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "page_count", nullable = false)
    private Integer pageCount;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "description", nullable = false)
    private String description;

}
