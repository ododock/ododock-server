package ododock.webserver.domain.content;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("movie")
public class Movie extends Content {

    @Column(name = "director", updatable = false)
    private String director;

    @Column(name = "genre")
    private String genre;

}
