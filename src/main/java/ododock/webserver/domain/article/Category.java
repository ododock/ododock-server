package ododock.webserver.domain.article;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "category",
        indexes = {
            @Index(name = "idx_category__account_id_last_modified_at", columnList = "account_id, last_modified_at desc"),
            @Index(name = "idx_category__category_id_position", columnList = "account_id, position desc")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Account ownerAccount;

    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH} // TODO 캐스케이딩 타입 확인
    )
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private List<Article> articles;

    @Column(name = "name", nullable = false)
    private String name;

    @PositiveOrZero
    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "visibility", nullable = false)
    private boolean visibility;

    @Builder
    public Category(final Account ownerAccount, final String name, final Boolean visibility) {
        ownerAccount.getCategories().add(this);
        this.name = name;
        this.position = ownerAccount.getCategories().size();
        this.visibility = visibility == null || visibility;
        this.articles = new ArrayList<>();
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
