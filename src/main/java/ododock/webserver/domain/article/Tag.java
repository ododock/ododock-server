package ododock.webserver.domain.article;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class Tag {

    @Column(name = "article_id", insertable = false, updatable = false)
    private Long articleId;

    @Column(name = "tag_name", nullable = false) // TODO nullable을 넣는게 맞나?
    private String name;

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
