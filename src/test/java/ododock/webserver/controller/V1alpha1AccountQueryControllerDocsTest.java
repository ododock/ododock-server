package ododock.webserver.controller;

import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestMvcSecurityConfig;
import ododock.webserver.config.web.WebConfiguration;
import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.account.AccountQueryService;
import ododock.webserver.domain.account.ProfileImage;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.V1alpha1AccountQueryController;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Account;
import ododock.webserver.web.v1alpha1.dto.response.AccountDetailsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(V1alpha1AccountQueryController.class)
@Import({RestDocsConfig.class, TestMvcSecurityConfig.class, WebConfiguration.class})
@AutoConfigureRestDocs
public class V1alpha1AccountQueryControllerDocsTest {

    private static final String BASE_PATH = ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountQueryService accountQueryService;

    @Test
    @WithMockUser
    void getAccount_Docs() throws Exception {
        // given
        final Account response = Account.builder()
                .id(1L)
                .email("tester@oddk.xyz")
                .nickname("tester")
                .birthDate(LocalDate.of(1999, 12, 31))
                .fullname("John doe")
                .profileImage(ProfileImage.builder()
                        .imageSource("http://oddk.xyz/foo.png")
                        .fileType("png")
                        .build())
                .build();

        when(accountQueryService.getAccountDetails(1L)).thenReturn(response);

        mockMvc.perform(
                        get(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/get-account-details",
                                resourceDetails()
                                        .tag("Account").description("계정 정보 조회").privateResource(true),
                                pathParameters(
                                        parameterWithName("id").description("조회할 Account ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("조회한 account ID"),
                                        fieldWithPath("email").description("조회한 Account 이메일"),
                                        fieldWithPath("password").description("조회한 Account 비밀번호"),
                                        fieldWithPath("fullname").description("조회한 Account 이름"),
                                        fieldWithPath("birthDate").description("조회한 Account 생년월일"),
                                        fieldWithPath("nickname").description("조회한 Account의 Profile 닉네임"),
                                        fieldWithPath("attributes").description("조회한 Account attributes"),
                                        fieldWithPath("providers").description("조회한 Account의 연동된 Social Accounts"),
                                        fieldWithPath("profileImageSource").description("조회한 Account의 프로필 이미지"),
                                        fieldWithPath("profileImageFileType").description("조회한 Account의 프로필 이미지"),
                                        fieldWithPath("emailVerified").description("조회한 Account의 이메일 인증 여부"),
                                        fieldWithPath("createdAt").description("조회한 Account 최초 생성일"),
                                        fieldWithPath("updatedAt").description("조회한 Account 최근 수정일")
                                )
                        )
                );
    }

}
