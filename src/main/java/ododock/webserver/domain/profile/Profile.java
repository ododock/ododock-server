package ododock.webserver.domain.profile;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.common.BaseEntity;
import ododock.webserver.domain.account.Account;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"PROFILE\"")
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;
//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name="uuid2", strategy = "uuid2")
//    @Column(name = "profile_id", columnDefinition = "BINARY(16)")
//    private UUID id;

    @OneToOne(mappedBy = "ownProfile")
    private Account ownerAccount;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Embedded
    private ProfileImage profileImage;

    @ElementCollection
    @CollectionTable(name="CATEGORIES", joinColumns=
        @JoinColumn(name="profile_id")
    )
    private Set<Category> categories = new HashSet<Category>();


}
