package ododock.webserver.domain.profile;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.article.Article;
import ododock.webserver.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile ownerProfile;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH}
    )
    @JoinColumn(
            name = "article_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private List<Article> articles = new ArrayList<>();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "visibility", nullable = false)
    private boolean visibility;

    @Builder
    public Category(final Profile ownerProfile, final String name, final boolean visibility) {
        ownerProfile.getCategories().add(this);
        this.ownerProfile = ownerProfile;
        this.name = name;
        this.visibility = visibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return visibility == category.visibility && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, visibility);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateVisibility(boolean visibility) {
        this.visibility = visibility;
    }

}
