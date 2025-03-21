package ododock.webserver.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.print.attribute.standard.Media;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "movie")
@DiscriminatorValue("movie")
public class Movie extends MediaContent {

    @Column(name = "director", updatable = false)
    private String director;

    @Column(name = "genre")
    private String genre;

}
