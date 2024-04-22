package ododock.webserver.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "book")
@DiscriminatorValue("book")
@NoArgsConstructor
public class Book extends Content {

    @Column(name = "isbn", updatable = false)
    private Long isbn;

}
