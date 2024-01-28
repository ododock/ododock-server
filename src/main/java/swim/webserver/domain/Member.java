package swim.webserver.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "\"Member\"")
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String username;

    private String password;

    @Builder
    public Member(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
