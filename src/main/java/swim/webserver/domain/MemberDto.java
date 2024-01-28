package swim.webserver.domain;

import lombok.Data;

@Data
public class MemberDto {
    private String email;
    private String username;
    private String password;
}
