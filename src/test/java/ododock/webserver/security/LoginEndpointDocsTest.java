package ododock.webserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.domain.account.Account;
import ododock.webserver.repository.AccountRepository;
import ododock.webserver.request.account.AccountCreate;
import ododock.webserver.request.account.CompleteDaoAccountRegister;
import ododock.webserver.request.account.RequestVerificationCode;
import ododock.webserver.response.account.AccountCreateResponse;
import ododock.webserver.security.request.LoginRequest;
import ododock.webserver.security.response.Token;
import ododock.webserver.service.AccountService;
import ododock.webserver.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
public class LoginEndpointDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MailService mailService;

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(ServletWebServerApplicationContext context, RestDocumentationContextProvider provider) throws Exception {
        RestAssured.port = context.getWebServer().getPort();
        RestAssured.baseURI = "http://127.0.0.1";

        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(provider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();

        AccountCreateResponse response = accountService.createDaoAccount(AccountCreate.builder()
                .nickname("test-user")
                .email("test-user@oddk.xyz")
                .fullname("testuser")
                .password("password")
                .birthDate(LocalDate.of(1993, 10, 23))
                .attributes(Map.of())
                .build());
        Account createdAccount = accountRepository.findById(response.sub()).orElseThrow(IllegalStateException::new);
        accountService.sendEmailVerificationCode(createdAccount.getId(), createdAccount.getEmail());
        Account foundAccount = accountRepository.findById(response.sub()).orElseThrow(IllegalStateException::new);
        accountService.activateDaoAccountRegister(
                createdAccount.getId(),
                CompleteDaoAccountRegister.builder()
                        .code(foundAccount.getVerificationInfo().getCode())
                        .email(foundAccount.getEmail())
                        .build());
    }

    @Test
    public void login_Docs(RestDocumentationContextProvider restDocumentation) throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("test-user@oddk.xyz")
                .password("password")
                .build();

        Account account = accountRepository.findByEmail(request.email())
                .orElseThrow(IllegalStateException::new);

        Response response = given(this.spec)
                .filter(document("dao-login",
                        resourceDetails().tag("Auth").description("DB 유저 로그인 엔드포인트"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("로그인할 유저 이메일"),
                                fieldWithPath("password").description("로그인할 유저 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("sub").description("로그인한 유저의 ID"),
                                fieldWithPath("accessToken").description("로그인한 유저의 액세스 토큰"),
                                fieldWithPath("refreshToken").description("로그인한 유저의 리프레시 토큰")
                        )
                ))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        Token token = objectMapper.readValue(response.getBody().asString(), Token.class);

        assertThat(Long.valueOf(token.sub()).equals(account.getId())).isTrue();
        assertThat(token.accessToken()).isNotBlank();
        assertThat(token.refreshToken()).isNotBlank();

    }

}
