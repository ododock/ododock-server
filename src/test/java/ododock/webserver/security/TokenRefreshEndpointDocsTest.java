package ododock.webserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.AccountService;
import ododock.webserver.domain.account.ProfileImage;
import ododock.webserver.domain.notification.MailService;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.security.filter.RefreshTokenAuthenticationFilter;
import ododock.webserver.security.response.DaoUserDetails;
import ododock.webserver.security.response.UserPrincipal;
import ododock.webserver.web.ResourcePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import({RestDocsConfig.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class TokenRefreshEndpointDocsTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @MockBean
    private MailService mailService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtDecoder jwtDecoder;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        RefreshTokenAuthenticationFilter filter = new RefreshTokenAuthenticationFilter(jwtDecoder, jwtService, objectMapper);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(print())
                .addFilters(filter)
                .build();
    }

    @Test
    @Transactional
    public void testRefreshTokenAuthentication() throws Exception {
        Account request = Account.builder()
                .fullname("John Doe")
                .nickname("testuser")
                .password("password")
                .email("test-user@oddk.xyz")
                .birthDate(LocalDate.of(1993, 10, 23))
                .attributes(Map.of())
                .profileImage(ProfileImage.builder()
                        .imageSource("image1")
                        .fileType("png")
                        .build())
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
        mockMvc.perform(
                        post(ResourcePath.AUTH_REFRESH_URL)
                                .cookie(new Cookie("refresh_token", refreshToken)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("refresh-token",
                                resourceDetails()
                                        .tag("Auth").description("리프레시 토큰을 통한 액세스/리프레시 토큰 갱신 엔드포인트"),
                                responseFields(
                                        fieldWithPath("sub").description("유저 ID"),
                                        fieldWithPath("accessToken").description("새로 발급된 액세스 토큰"),
                                        fieldWithPath("refreshToken").description("새로 발급된 리프레시 토큰")
                                )
                        )
                );
    }

}