package ododock.webserver.security;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.web.ResourcePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
public class LogoutEndpointDocsTest {

    private static final String BASE_URL = ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.AUTH;
    private RequestSpecification spec;

    @BeforeEach
    public void setUp(ServletWebServerApplicationContext context, RestDocumentationContextProvider provider) {
        RestAssured.port = context.getWebServer().getPort();
        RestAssured.baseURI = "http://127.0.0.1";

        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(provider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    public void logout_Docs() throws Exception {
        given(this.spec)
                .filter(document("security-logout",
                        resourceDetails().tag("Auth").description("로그아웃 엔드포인트"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .when()
                .post(BASE_URL + ResourcePath.LOGOUT)
                .then()
                .statusCode(HttpStatus.FOUND.value())
                .extract()
                .response();

    }

}
