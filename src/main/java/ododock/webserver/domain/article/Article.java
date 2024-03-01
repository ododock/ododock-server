package ododock.webserver.domain.article;

import jakarta.annotation.Nullable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.profile.Category;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = false)
    private String body;

    @ElementCollection
    @CollectionTable(name="tags", joinColumns=
        @JoinColumn(name="article_id", nullable = true)
    )
    private Set<Tag> tags = new HashSet<Tag>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Article(String title, String body, @Nullable Set<String> tags, @Nullable Category category) {
        this.title = title;
        this.body = body;
        this.tags.addAll(tags.stream()
                .map(Tag::new)
                .collect(Collectors.toSet()));
        if (category != null) {
            category.getArticles().add(this);
            this.updateCategory(category);
        }
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateBody(String body) {
        this.body = body;
    }

    public void updateCategory(Category category) {
        category.getArticles().add(this);
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
