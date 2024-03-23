package ododock.webserver.domain.profile;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.article.Article;
import ododock.webserver.domain.common.BaseEntity;
import ododock.webserver.domain.account.Account;

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

    @OneToOne
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Account ownerAccount;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Embedded
    private ProfileImage profileImage;

    @OneToMany(
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
//    @OrderColumn(name = "order")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "article_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private List<Article> articles = new ArrayList<>();

    @Builder
    public Profile(final Account ownerAccount, final String nickname, final String imageSource, final String fileType) {
        this.ownerAccount = ownerAccount;
        this.nickname = nickname;
        this.profileImage = ProfileImage.builder()
                .imageSource(imageSource)
                .fileType(fileType)
                .build();
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateCategories(final List<Category> categories) {
        this.categories = categories;
    }
    public void updateProfileImage(final String imageSource, final String filetype) {
        this.profileImage.updateImageSource(imageSource);
        this.profileImage.updateFileType(filetype);
    }

}
