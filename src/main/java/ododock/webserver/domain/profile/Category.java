package ododock.webserver.domain.profile;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.article.Article;
import ododock.webserver.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "category",
        indexes = {
            @Index(name = "idx_category__profile_id_last_modified_at", columnList = "profile_id, last_modified_at desc")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Profile ownerProfile;

    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH} // TODO 캐스케이딩 타입 확인
    )
    private List<Article> articles = new ArrayList<>();

    @Column(name = "name", nullable = false)
    private String name;

    @PositiveOrZero
    @Column(name = "category_order")
    private Integer order;

    @Column(name = "visibility", nullable = false)
    private boolean visibility;

    @Builder
    public Category(final Profile ownerProfile, final String name, final boolean visibility) {
        ownerProfile.getCategories().add(this);
        ownerProfile.updateCategoriesSize(ownerProfile.getCategorySize()+1);
        this.ownerProfile = ownerProfile;
        this.name = name;
        this.order = ownerProfile.getCategorySize();
        this.visibility = visibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return visibility == category.visibility && Objects.equals(name, category.name) && Objects.equals(order, category.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, order, visibility);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void updateOrder(Integer order) {
        this.order = order;
    }

}
