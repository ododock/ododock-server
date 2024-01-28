package swim.webserver.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    @Id
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int seq;

    @Column(name = "username")
    private String username;

    @Column(name = "authority")
    private String authority;

}
