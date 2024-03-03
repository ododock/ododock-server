package ododock.webserver.domain.article;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "template")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Template { // TODO article과 같을텐데, 따로 엔티티화가 필요할까?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @NotNull
    private String body;

}
