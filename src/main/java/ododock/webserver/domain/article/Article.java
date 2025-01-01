package ododock.webserver.domain.article;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.BaseEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "article",
        indexes = {
                @Index(name = "idx_article__profile_id_last_modified_at", columnList = "profile_id, last_modified_at desc")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

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

    @Column(name = "visibility", nullable = false)
    private boolean visibility;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id",
            nullable = false, updatable = false, insertable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Account ownerAccount;

    @Nullable
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Category category;

    @ElementCollection
    @CollectionTable(name = "article_tags",
            joinColumns = @JoinColumn(name = "article_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    )
    private Set<Tag> tags;

    @Builder
    public Article(final String title,
                   final String body,
                   final Set<String> tags,
                   @Nullable final Category category,
                   final boolean visibility) {
        this.title = title;
        this.body = body;
        this.tags = tags.stream()
                .filter(tag -> !tag.isBlank())
                .map(Tag::new).collect(Collectors.toSet());
        if (category != null) {
            category.getArticles().add(this);
            this.updateCategory(category);
        }
        this.visibility = visibility;
    }

    public void updateTitle(final String title) {
        this.title = title;
    }

    public void updateBody(final String body) {
        this.body = body;
    }

    public void updateCategory(@Nullable final Category category) {
        if (category == null) {
            this.category = null;
            return;
        }
        category.getArticles().add(this);
        this.category = category;

    }

    public void updateTags(final Set<String> tags) {
        Set<Tag> newTags = tags.stream()
                .map(Tag::new)
                .collect(Collectors.toSet());
        if (!this.tags.equals(newTags)) {
            this.tags.clear();
            this.tags.addAll(newTags);
        }
    }

    public void updateVisibility(final boolean visibility) {
        this.visibility = visibility;
    }

    public Set<String> getTags() {
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }

}
