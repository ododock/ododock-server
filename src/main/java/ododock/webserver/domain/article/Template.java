package ododock.webserver.domain.article;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ododock.webserver.domain.profile.Profile;

@Entity
@Getter
@Table(name = "template")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    private String body;

    @NotNull
    private Boolean visibility;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id",
            nullable = false, updatable = false, insertable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Profile ownerProfile;

}
