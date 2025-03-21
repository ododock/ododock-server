package ododock.webserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.AccountManageService;
import ododock.webserver.domain.account.AccountService;
import ododock.webserver.domain.account.ProfileImage;
import ododock.webserver.domain.notification.MailService;
import ododock.webserver.domain.verification.VerificationInfo;
import ododock.webserver.domain.verification.VerificationService;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.repository.jpa.VerificationInfoRepository;
import ododock.webserver.security.request.LoginRequest;
import ododock.webserver.security.response.Token;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.account.CompleteDaoAccountVerification;
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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import({RestDocsConfig.class})
@AutoConfigureRestDocs
public class LoginEndpointDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountManageService accountManageService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationInfoRepository verificationInfoRepository;

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

        Account request = Account.builder()
                .email("john.doe@oddk.xyz")
                .password("password")
                .fullname("John Doe")
                .birthDate(LocalDate.of(1997, 1, 23))
                .nickname(("johnDoe123"))
                .profileImage(ProfileImage.builder()
                        .imageSource("image1")
                        .fileType(".png")
                        .build())
                .build();

        accountService.createDaoAccount(request);

        Account createdAccount = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(IllegalStateException::new);
        verificationService.issueVerificationCode(request.getEmail());

        VerificationInfo verificationInfo = verificationInfoRepository.findByTargetEmail(createdAccount.getEmail())
                .orElseThrow(IllegalStateException::new);

        accountManageService.verifyDaoAccountEmail(
                CompleteDaoAccountVerification.builder()
                        .verificationCode(verificationInfo.getCode())
                        .email(request.getEmail())
                        .build());
    }

    @Test
    public void login_Docs(RestDocumentationContextProvider restDocumentation) throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("john.doe@oddk.xyz")
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
                .post(ResourcePath.AUTH_PROCESSING_URL)
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
