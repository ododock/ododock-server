package ododock.webserver.domain.account;

import com.querydsl.core.util.StringUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.AttributesMapConverter;
import ododock.webserver.domain.article.Article;
import ododock.webserver.domain.article.Category;
import ododock.webserver.domain.article.Template;
import ododock.webserver.domain.BaseEntity;
import ododock.webserver.web.exception.ResourceNotFoundException;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
@Table(
        name = "\"account\"",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_account__email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_account__last_modified_at", columnList = "last_modified_at desc")
        }
)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Embedded
    private Profile ownProfile;

    @OneToMany(
            mappedBy = "daoAccount",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<SocialAccount> socialAccounts;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Nullable
    @Column(name = "attributes")
    @Convert(converter = AttributesMapConverter.class)
    private Map<String, List<String>> attributes;

    @Column(name = "emailVerified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialNonExpired;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    // todo role binding으로 변경 필요
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(
            mappedBy = "ownerAccount",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @OrderBy("position asc")
    private List<Category> categories;

    @OneToMany(
            mappedBy = "ownerAccount",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @OrderBy("createdDate desc")
    private List<Article> articles;

    @OneToMany(
            mappedBy = "ownerAccount",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<Template> templates;

    @Builder
    public Account(
            final String email,
            final String password,
            final String fullname,
            final LocalDate birthDate,
            final Set<Role> roles,
            final String nickname,
            final Map<String, List<String>> attributes,
            final ProfileImage profileImage
    ) {
        this.email = email;
        this.password = password;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialNonExpired = true;
        this.enabled = true;
        this.emailVerified = false;
        this.roles = new HashSet<>(roles);
        this.attributes = new HashMap<>();
        this.ownProfile = Profile.builder()
                .nickname(nickname)
                .fullname(fullname)
                .birthDate(birthDate)
                .profileImage(profileImage)
                .build();
        this.socialAccounts = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.articles = new ArrayList<>();
        this.templates = new ArrayList<>();
    }

    public void updatePassword(final String password) {
        this.password = password;
    }

    public void updateAttributes(final Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public void activate() {
        if (StringUtils.isNullOrEmpty(this.email)) {
            throw new IllegalArgumentException("email cannot be null");
        }
        if (StringUtils.isNullOrEmpty(this.password)) {
            throw new IllegalArgumentException("password cannot be null");
        }
        if (StringUtils.isNullOrEmpty(this.ownProfile.getNickname())) {
            throw new IllegalArgumentException("nickname cannot be null");
        }
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void setProfile(final Profile profile) {
        this.ownProfile = profile;
    }

    public void updateRoles(final Set<Role> updatedRoles) {
        this.roles.clear();
        this.roles.addAll(updatedRoles);
    }

    public void addSocialAccount(final SocialAccount socialAccount) {
        this.socialAccounts.add(socialAccount);
    }

    public void removeSocialAccount(final SocialAccount socialAccount) {
        this.socialAccounts.remove(socialAccount);
    }

    public void deleteCategory(final Category category) {
        if (this.categories.isEmpty()) {
            throw new IllegalStateException("category is empty");
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
        }
    }

}
