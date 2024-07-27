package ododock.webserver.domain.profile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.article.Article;
import ododock.webserver.domain.common.BaseEntity;
import ododock.webserver.exception.ResourceNotFoundException;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "profile",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_profile__account_nickname", columnNames = {"account_id", "nickname"}),
        },
        indexes = {
                @Index(name = "idx_profile__last_modified_at", columnList = "last_modified_at desc")
        }
)
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Account ownerAccount;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Nullable
    @Embedded
    private ProfileImage profileImage;

    @OneToMany(
            mappedBy = "ownerProfile",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @OrderBy("order asc")
    private List<Category> categories = new ArrayList<>();

    @Column(name = "category_size")
    private Integer categorySize;

    @OneToMany(
            mappedBy = "ownerProfile",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<Article> articles = new ArrayList<>();

    @Builder
    public Profile(final String nickname, final ProfileImage profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.categorySize = 0;
    }

    public void setOwnerAccount(final Account account) {
        this.ownerAccount = account;
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateCategoriesSize(final Integer categorySize) {
        this.categorySize = categorySize;
    }

    public void deleteCategory(final Category category) {
        if (this.categories.isEmpty()) {
            throw new IllegalArgumentException("category is empty");
        }
        Category foundCategory = this.categories.stream()
                .filter(c -> c.equals(category))
                .findAny()
                .orElseThrow(
                        () -> new ResourceNotFoundException(Category.class, category.getId())
                );
        if (!foundCategory.getArticles().isEmpty()) {
            List<Article> articles = foundCategory.getArticles();
            articles.forEach(article -> article.updateCategory(null));
            this.categorySize--;
        }
    }

    public void updateProfileImage(final String imageSource, final String filetype) {
        this.profileImage = ProfileImage.builder()
                .imageSource(imageSource)
                .fileType(filetype)
                .build();
    }

}
