package ododock.webserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.TokenRecord;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.request.account.AccountCreate;
import ododock.webserver.security.filter.RefreshTokenAuthenticationFilter;
import ododock.webserver.security.response.UserPrincipal;
import ododock.webserver.security.service.JwtService;
import ododock.webserver.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import({RestDocsConfig.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class TokenRefreshEndpointDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        RefreshTokenAuthenticationFilter filter = new RefreshTokenAuthenticationFilter(jwtDecoder, jwtService, objectMapper);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new Object()) // 더미 컨트롤러
                .addFilter(filter)
                .build();
    }

    @Test
    @Transactional
    public void testRefreshTokenAuthentication() throws Exception {
        AccountCreate request = AccountCreate.builder()
                .fullname("John Doe")
                .nickname("testuser")
                .password("password")
                .email("test-user@oddk.xyz")
                .birthDate(LocalDate.of(1993, 10, 23))
                .attributes(Map.of())
                .build();
        accountService.createDaoAccount(request);

        Account account = accountRepository.findByEmail("test-user@oddk.xyz")
                .orElseThrow(IllegalStateException::new);


        DaoUserDetails details = DaoUserDetails.builder().account(account).build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

        TokenRecord tokenRecord = jwtService.generateToken(UserPrincipal.from((DaoUserDetails) authentication.getPrincipal()));
        String refreshToken = tokenRecord.getRefreshTokenValue();


        // When & Then
        mockMvc.perform(post("/api/v1/login/refresh")
                        .cookie(new Cookie("refresh_token", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new_access_token"))
                .andDo(print())
                .andDo(document("refresh-token",
                        responseFields(
                                fieldWithPath("token").description("새로 발급된 액세스 토큰")
                        )
                ));

    }

    @Test
    public void testRefreshTokenAuthentication_InvalidToken() throws Exception {
        // Given
        String invalidRefreshToken = "invalid_refresh_token";

        given(jwtDecoder.decode(anyString())).willThrow(new Exception("Invalid token"));

        // When & Then
        mockMvc.perform(post("/api/v1/login/refresh")
                        .cookie(new Cookie("refresh_token", invalidRefreshToken)))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("refresh-token-invalid",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("error").description("에러 메시지")
                        )
                ));
    }
}