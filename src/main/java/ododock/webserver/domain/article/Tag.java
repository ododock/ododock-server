package ododock.webserver.domain.article;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@RequiredArgsConstructor
public class Tag {

    @Column(name = "article_id", insertable = false, updatable = false)
    private Long articleId;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public Tag(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
