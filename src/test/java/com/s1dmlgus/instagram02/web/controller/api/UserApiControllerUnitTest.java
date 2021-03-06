package com.s1dmlgus.instagram02.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1dmlgus.instagram02.config.SecurityConfig;
import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.config.auth.WithMockCustomUser;
import com.s1dmlgus.instagram02.domain.user.Role;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.handler.exception.CustomValidationException;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import com.s1dmlgus.instagram02.web.dto.user.profile.UserProfileResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.update.UserUpdateResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentRequest;
import static com.s1dmlgus.instagram02.common.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(value = UserApiController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class UserApiControllerUnitTest {

    Logger logger = LoggerFactory.getLogger(UserApiControllerUnitTest.class);

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private PrincipalDetails principal;


    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

    }


    @DisplayName("???????????? ?????????")
    @Test
    public void joinUserTest() throws Exception{
        //given
        JoinRequestDto joinRequestDto = new JoinRequestDto("t1dmlgus", "1234", "test@gamil.com", "?????????");
        String json = new ObjectMapper().writeValueAsString(joinRequestDto);

        User joinUser = User.builder()
                .id(1L)
                .username("t1dmlgus")
                .email("test@gmail.com")
                .name("?????????")
                .build();
        joinUser.setRole(Role.ROLE_USER);


        //when
        doReturn(new ResponseDto<>("??????????????? ??????????????? ???????????????.", new JoinResponseDto(joinUser)))
                .when(userService).join(joinRequestDto);


        ResultActions resultActions = mockMvc.perform(
                post("/api/user/join")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
        );


        //then(??????)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("??????????????? ??????????????? ???????????????."))
                .andDo(print())
                .andDo(document("{class-name}/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("??????")
                        )
                ));

    }



    @DisplayName("???????????? username ????????? ?????? ?????? ?????? ?????????")
    @Test
    public void joinDtoUsernameExceptionTest() throws Exception {

        //given(??????)
        JoinRequestDto joinRequestDto = new JoinRequestDto("t22222222222222222222222222222222222dmlgus", "1234", "test@gamil.com", "???????????????");
        String json = new ObjectMapper().writeValueAsString(joinRequestDto);

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("username", "0?????? 20????????? ????????? ??? ????????????");

        // new ResponseDto -> data ??? ????????????.
        when(userService.join(joinRequestDto)).thenThrow(new CustomValidationException("????????? ?????? ??????", errorMap));

        //when(?????????)
        ResultActions resultAction =
                mockMvc.perform(
                        post("/api/user/join")
                                .with(csrf())
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)

                );

        //then(??????)
        resultAction
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("????????? ?????? ??????"))
                .andExpect(jsonPath("$.data.username").value("0?????? 20????????? ????????? ??? ????????????"))
                .andDo(MockMvcResultHandlers.print());

    }


    
    @DisplayName("????????? ?????? ?????? ?????????")
    @WithMockCustomUser()
    @Test
    public void getProfileUserTest() throws Exception{
        //given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        principal = (PrincipalDetails) authentication.getPrincipal();

        User user = User.builder()
                .id(2L)
                .username("t1dmlgus")
                .email("test@gmail.com")
                .name("?????????")
                .bio("??????????????????")
                .website("https://test.com")
                .build();


        long pageId = 2L;

        doReturn(new ResponseDto<>("?????? ????????? ???????????????.", new UserProfileResponseDto(user, true, false, 0)))
                .when(userService).profile(pageId, principal);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/user/{id}",pageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .characterEncoding("utf-8")

        );

        //then
        resultActions
                //.andExpect(status().isOk())
                //.andExpect(jsonPath("$.message").value("?????? ????????? ???????????????."))
                .andDo(print())
                .andDo(document("{class-name}/{method-name}",getDocumentRequest(), getDocumentResponse()
        ));

    }

    
    // BDDMockito ??????
    @DisplayName("???????????? ?????? ?????????")
    @Test
    @WithMockCustomUser()
    public void updateUserTest() throws Exception{
        //given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        principal = (PrincipalDetails) authentication.getPrincipal();

        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto("?????????", "update.com", "???????????????!", "03033", "???");
        String json = new ObjectMapper().writeValueAsString(updateRequestDto);

        User updateUser = User.builder()
                .id(principal.getUser().getId())
                .username(principal.getUsername())
                .name(updateRequestDto.getName())
                .website(updateRequestDto.getWebsite())
                .bio(updateRequestDto.getBio())
                .phone(updateRequestDto.getPhone())
                .build();


        doReturn(new ResponseDto<>("?????? ????????? ?????????????????????.", new UserUpdateResponseDto(updateUser)))
                .when(userService).update(updateRequestDto, principal);



        //when
        ResultActions resultActions = mockMvc.perform(
                put("/api/user/update")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("?????? ????????? ?????????????????????."))
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", getDocumentRequest(), getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("website").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("bio").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("??????")
                        )
                ));

    }

}