package ododock.webserver.domain.account;

import jakarta.persistence.*;
import lombok.*;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.domain.common.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
@Table(name = "\"ACCOUNT\"")
//@ToString(of = {"username", "fullname"})
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name="uuid2", strategy = "uuid2")
//    @Column(name = "account_id", columnDefinition = "BINARY(16)")
//    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false) // TODO what is optional field -> false이면 항상 연관 객체가 있어야함
    @JoinColumn(name = "profile_id")
    private Profile ownProfile;

    private String email;

    private String username;

    private String password;

    private String fullname;

    private Boolean enabled;

    private Boolean accountLocked;

    private Boolean accountExpired;

    private LocalDate birthDate;

//    @ManyToOne
//    private List<Authority> Authorities;
    @Builder
    public Account(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
