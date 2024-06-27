package ododock.webserver.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.security.handler.OAuth2LoginSuccessHandler;
import ododock.webserver.security.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import({RestDocsConfig.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class SocialLoginSuccessResultResponseDocs {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private OAuth2LoginSuccessHandler successHandler;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) throws ServletException, IOException {
        successHandler = new OAuth2LoginSuccessHandler(jwtService);

        OAuth2LoginAuthenticationFilter mockFilter = Mockito.mock(OAuth2LoginAuthenticationFilter.class);
        Mockito.doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain filterChain = invocation.getArgument(2);

            OAuth2User oAuth2User = new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    Map.of("sub", "1", "accountId", "1", "authorizedClientRegistrationId", "google"),
                    "sub"
            );

            OAuth2AuthenticationToken authenticationToken = new OAuth2AuthenticationToken(
                    oAuth2User,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    "google"
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            successHandler.onAuthenticationSuccess(request, response, authenticationToken);
            return null;
        }).when(mockFilter).doFilter(Mockito.any(ServletRequest.class), Mockito.any(ServletResponse.class), Mockito.any(FilterChain.class));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(print())
                .addFilters(mockFilter)
                .build();
    }

    @Test
    public void testOAuth2LoginSuccess() throws Exception {
        mockMvc.perform(get("/login/oauth2/code/google"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> {
                    String location = result.getResponse().getHeader("Location");
                    UriComponents uriComponents = UriComponentsBuilder.fromUriString(location).build();
                    MultiValueMap<String, String> queryParams = uriComponents.getQueryParams();

                    assertThat(queryParams.getFirst("sub")).isEqualTo("1");
                    assertThat(queryParams.getFirst("provider")).isEqualTo("google");
                    assertThat(queryParams.getFirst("access_token")).isNotBlank();
                    assertThat(queryParams.getFirst("refresh_token")).isNotBlank();
                })
                .andDo(document("login-oauth2-code-google",
                        resourceDetails().tag("Auth")
                                .description("소셜 로그인 성공 시 callback주소로 query param에 아래의 헤더 정보가 담겨 리다이렉트 됨."),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseHeaders(
                                headerWithName("Location").description("The URL to redirect to with query parameters: "
                                        + "`sub` - 소셜 로그인한 유저 ID, "
                                        + "`provider` - 소셜 로그인한 유저의 provider, "
                                        + "`access_token` - 소셜 로그인한 유저의 액세스 토큰, "
                                        + "`refresh_token` - 소셜 로그인한 유저의 리프레시 토큰")
                        )));
    }

}
