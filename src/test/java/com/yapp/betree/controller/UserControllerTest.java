package com.yapp.betree.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.betree.dto.response.UserResponseDto;
import com.yapp.betree.service.JwtTokenTest;
import com.yapp.betree.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.yapp.betree.domain.UserTest.TEST_SAVE_USER;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("User 컨트롤러 테스트")
@WebMvcTest(UserController.class)
public class UserControllerTest extends ControllerTest{

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("유저 정보 조회")
    @Test
    void getUserInfo() throws Exception {

        given(userService.getUser(TEST_SAVE_USER.getId())).willReturn(UserResponseDto.of(TEST_SAVE_USER));

        mockMvc.perform(get("/api/user")
                        .header("Authorization", "Bearer " + JwtTokenTest.JWT_TOKEN_TEST))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

}
