package ododock.webserver.domain.account;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Nullable
    private Long seq;

    private Long userId;

    @Column(name = "authority")
    private String authority;

    public Authority(Long userId, String authority) {
        this.userId = userId;
        this.authority = authority;
    }
}
