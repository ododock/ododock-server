package ododock.webserver.domain.account;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
@Table(
        name = "account",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_account__email", columnNames = "email")
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
            fetch = FetchType.LAZY,
            optional = false,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Profile ownProfile;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fullname", nullable = false, updatable = false)
    private String fullname;

    @Column(name = "birth_date", nullable = false, updatable = false)
    private LocalDate birthDate;

    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialNonExpired;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @Builder
    public Account(
            final String nickname,
            final String imageSource,
            final String fileType,
            final String email,
            final String password,
            final String fullname,
            final LocalDate birthDate,
            final List<String> roles
    ) {
        this.ownProfile = new Profile(this, nickname, imageSource,fileType);
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.birthDate = birthDate;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialNonExpired = true;
        this.enabled = true;
        this.roles.addAll(roles.stream().map(Role::new).toList());
    }

    public void updatePassword(final String password) {
        this.password = password;
    }

    public void updateRoles(final List<Role> updateRoles) {
        this.roles = roles;
    }

}
