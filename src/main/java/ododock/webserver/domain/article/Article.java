package ododock.webserver.domain.article;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "\"ARTICLE\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;
//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name="uuid2", strategy = "uuid2")
//    @Column(name = "article_id", columnDefinition = "BINARY(16)")
//    private UUID id;

    @ElementCollection
    @CollectionTable(name="TAGS", joinColumns=
        @JoinColumn(name="article_id")
    )
    private Set<Tag> tags = new HashSet<Tag>();

}
