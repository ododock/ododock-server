package ododock.webserver.domain.account;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.domain.common.BaseEntity;
import ododock.webserver.domain.profile.ProfileImage;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@ToString(of = {"email", "fullname"})
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @OneToOne(
            mappedBy = "ownerAccount",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Profile ownProfile;

    @OneToMany(
            mappedBy = "ownerAccount",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<OAuth2Account> socialAccounts = new ArrayList<>();


    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Nullable
    @Column(name = "fullname")
    private String fullname;

    @Nullable
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialNonExpired;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Builder
    public Account(
            final String email,
            final String password,
            final String fullname,
            final LocalDate birthDate,
            final Set<Role> roles,
            final String nickname,
            final ProfileImage profileImage
            ) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.birthDate = birthDate;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialNonExpired = true;
        this.enabled = true;
        this.roles.addAll(roles);
        this.ownProfile = Profile.builder()
                .nickname(nickname)
                .profileImage(profileImage)
                .build();
        this.ownProfile.setOwnerAccount(this);
    }

    public void updatePassword(final String password) {
        this.password = password;
    }

    public void setProfile(final Profile profile) {
        this.ownProfile = profile;
        this.ownProfile.setOwnerAccount(this);
    }

    public void updateRoles(final Set<Role> updatedRoles) {
        this.roles.clear();
        this.roles.addAll(updatedRoles);
    }

    public void addSocialAccount(final OAuth2Account oAuth2Account) {
        this.socialAccounts.add(oAuth2Account);
    }

}
