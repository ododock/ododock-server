package ododock.webserver.domain.article;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.BaseEntity;
import ododock.webserver.domain.article.dto.V1alpha1BaseBlock;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Document(collection = "article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    private static final int MAX_LENGTH = 200;

    @Id
    @Column(name = "article_id")
    @MongoId
    private String id;

    @Version
    @Field(name = "version")
    private Long version;

    @Field(name = "title")
    private String title;

    @Field(name = "body")
    @Nullable
    private List<V1alpha1BaseBlock> body = new ArrayList<>();

    @Field(name = "excerpt")
    @Nullable
    private String excerpt;

    @Field(name = "plainText")
    @Nullable
    private String plainText;

    @Field(name = "visibility")
    private boolean visibility;

    @Field(name = "ownerAccountId")
    private Long ownerAccountId;

    @Nullable
    @Field(name = "categoryId")
    private String categoryId;

    @Field(name = "tags")
    private Set<Tag> tags = new HashSet<>();

    @Builder
    public Article(final String title,
                   final List<V1alpha1BaseBlock> body,
                   final Set<String> tags,
                   final Long ownerAccountId,
                   @Nullable final String categoryId,
                   final boolean visibility) {
        this.title = title;
        this.body = body;
        if (body != null && !body.isEmpty()) {
            plainText = ArticleExcerptor.from(body, null);
            excerpt = ArticleExcerptor.from(body, MAX_LENGTH);
        }
        this.tags = tags == null ? new HashSet<>() : tags.stream()
                .filter(tag -> !tag.isBlank())
                .map(Tag::new).collect(Collectors.toSet());
        this.ownerAccountId = ownerAccountId;
        this.categoryId = categoryId;
        this.visibility = visibility;
    }

    public void updateExcerpt() {
        this.excerpt = ArticleExcerptor.from(this.body, 200);
    }

    public void updatePlainText() {
        this.excerpt = ArticleExcerptor.from(this.body, null);
    }

    public void updateTitle(final String title) {
        this.title = title;
    }

    public void updateBody(final List<V1alpha1BaseBlock> body) {
        this.body = body;
    }

    public void updateCategory(@Nullable final String categoryId) {
        this.categoryId = categoryId;
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
