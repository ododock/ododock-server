package ododock.webserver.domain.article;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Getter
@Document(collection = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private String id;

    @Version
    @Field(name = "version")
    private Long version;

    @Field(name = "owner_account_id")
    private Long ownerAccountId;

    @Field(name = "name")
    private String name;

    @PositiveOrZero
    @Field(name = "position")
    private Integer position;

    @Field(name = "visibility")
    private boolean visibility;

    @Builder
    public Category(final String id, final Long ownerAccountId, final String name, final Boolean visibility, final Integer position) {
        this.id = id;
        this.ownerAccountId = ownerAccountId;
        this.name = name;
        this.visibility = visibility == null || visibility;
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return visibility == category.visibility && Objects.equals(name, category.name) && Objects.equals(position, category.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position, visibility);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void updatePosition(Integer position) {
        this.position = position;
    }

}
