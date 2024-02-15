package ododock.webserver.domain.account;

import lombok.Data;

@Data
public class AccountDto {
    private String email;
    private String username;
    private String password;
}
