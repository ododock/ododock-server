package ododock.webserver.domain.article;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "\"ARTICLE\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "body", nullable = false)
    private String body;

    @ElementCollection
    @CollectionTable(name="TAGS", joinColumns=
        @JoinColumn(name="article_id", nullable = true)
    )
    private Set<Tag> tags = new HashSet<Tag>();

    @Nullable
    @Column(name = "category", nullable = true)
    private String category;

    @Builder
    public Article(String title, String body, Set<String> tags, String category) {
        this.title = title;
        this.body = body;
        this.tags.addAll(tags.stream()
                .map(Tag::new)
                .collect(Collectors.toSet()));
        this.category = category;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateBody(String body) {
        this.body = body;
    }

    public void updateCategory(String category) {
        this.category = category;
    }

    public void updateTags(Set<String> tags) {
        Set<Tag> newTags = tags.stream()
                .map(Tag::new)
                .collect(Collectors.toSet());
        if (!this.tags.equals(newTags)) {
            this.tags.clear();
            this.tags.addAll(newTags);
        }
    }

}
