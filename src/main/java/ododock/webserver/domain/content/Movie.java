package ododock.webserver.domain.content;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
//@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("movie")
public class Movie extends Content {
    private String director;

}
