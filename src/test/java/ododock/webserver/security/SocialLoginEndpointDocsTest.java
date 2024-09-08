package ododock.webserver.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ododock.webserver.common.RestDocsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import({RestDocsConfig.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class SocialLoginEndpointDocsTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        MockOAuth2AuthorizationRequestRedirectFilter filter = new MockOAuth2AuthorizationRequestRedirectFilter();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(print())
                .addFilters(filter)
                .build();
    }

    public class MockOAuth2AuthorizationRequestRedirectFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            if (request.getRequestURI().startsWith("/oauth2/authorization/")) {
                response.setStatus(HttpStatus.FOUND.value());
                response.setHeader("Location", "https://accounts.google.com/o/oauth2/auth?client_id=...");
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    @Test
    public void socialLoginSuccessResultResponse_Docs() throws Exception {

        mockMvc.perform(get("/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", startsWith("https://accounts.google.com/o/oauth2/auth")))
                .andDo(document("oauth2-login-success-result",
                        resourceDetails().tag("Auth")
                                .description("/oauth2/authorization/{provider} 주소에 대해 하이퍼링크로 요청함."
                                        + "사용자는 하이퍼링크를 통해 oauth provider가 제공하는 로그인창으로 이동하여 로그인을 수행함."
                                        + "로그인 성공 시 `소셜로그인 성공 결과`처럼 callback 주소의 param과함께 redirect됨."))
                );
    }

}
