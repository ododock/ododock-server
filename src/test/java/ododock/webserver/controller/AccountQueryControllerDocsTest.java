package ododock.webserver.controller;

import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestSecurityConfig;
import ododock.webserver.domain.profile.ProfileImage;
import ododock.webserver.response.account.AccountDetailsResponse;
import ododock.webserver.service.AccountQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountQueryController.class)
@Import({RestDocsConfig.class, TestSecurityConfig.class})
@AutoConfigureRestDocs
public class AccountQueryControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountQueryService accountQueryService;

    @Test
    void getAccount_Docs() throws Exception {
        // given
        final AccountDetailsResponse response = AccountDetailsResponse.builder()
                .sub(1L)
                .profileId(6L)
                .email("tester@ododock.io")
                .nickname("tester")
                .birthDate(LocalDate.of(1999, 12, 31))
                .fullname("John doe")
                .profileImage(ProfileImage.builder()
                        .imageSource("http://awesome.io/foo.png")
                        .fileType("png")
                        .build())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .isDaoSignedUp(true)
                .providers(Set.of("google", "naver"))
                .build();

        when(accountQueryService.getAccountDetails(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/accounts/{accountId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/get-account-details",
                        pathParameters(
                                parameterWithName("accountId").description("조회할 Account ID")
                        ),
                        responseFields(
                                fieldWithPath("sub").description("조회한 Account ID"),
                                fieldWithPath("profileId").description("조회한 Profile ID"),
                                fieldWithPath("email").description("조회한 Account 이메일"),
                                fieldWithPath("fullname").description("조회한 Account 이름"),
                                fieldWithPath("birthDate").description("조회한 Account 생년월일"),
                                fieldWithPath("createdDate").description("조회한 Account 최초 생성일"),
                                fieldWithPath("lastModifiedDate").description("조회한 Account 최근 수정일"),
                                fieldWithPath("isDaoSignedUp").description("조회한 Account의 DB 설정 여부"),
                                fieldWithPath("nickname").description("조회한 Account의 Profile 닉네임"),
                                fieldWithPath("providers").description("조회한 Account의 연동된 Social Accounts"),
                                fieldWithPath("profileImage").description("조회한 Account의 프로필 이미지"),
                                fieldWithPath("profileImage.imageSource").description("조회한 Account의 프로필 이미지"),
                                fieldWithPath("profileImage.fileType").description("조회한 Account의 프로필 이미지")
                        ))
                );


    }


}
