package org.example.expert.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.expert.config.*;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestWebConfig.class)
@WebMvcTest(
        value = CommentController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)}
)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private CommentService commentService;

    @Test
    public void Comment_생성() throws Exception {
        //given
        long todoId = 1L;
        long commentId = 1L;
        long userId = 1L;

        String token = jwtUtil.createToken(userId, "email", UserRole.ADMIN);

        AuthUser authUser = new AuthUser(1L, "email", UserRole.ADMIN);

        CommentSaveRequest request = new CommentSaveRequest("contents");
        CommentSaveResponse response = new CommentSaveResponse(
                commentId,
                request.getContents(),
                new UserResponse(1L, "email")
        );

        given(commentService.saveComment(authUser, todoId, request)).willReturn(response);
        String body = mapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(post("/todos/" + todoId + "/comments")
                        .header("Authorization", token)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.contents").value(request.getContents()))
                .andExpect(jsonPath("$.user.id").value(authUser.getId()))
                .andExpect(jsonPath("$.user.email").value(authUser.getEmail()));
    }

    @Test
    public void Comment_todoId로_전체조회() throws Exception {
        //given
        long todoId = 1L;

        long commentId1 = 1L;
        long commentId2 = 2L;

        AuthUser authUser = new AuthUser(1L, "email", UserRole.ADMIN);
        User user = User.fromAuthUser(authUser);
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail());

        List<CommentResponse> commentList = List.of(
                new CommentResponse(commentId1, "contents1", userResponse),
                new CommentResponse(commentId2, "contents2", userResponse)
        );
        given(commentService.getComments(todoId)).willReturn(commentList);

        //when & then
        mockMvc.perform(get("/todos/" + todoId + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(commentId1))
                .andExpect(jsonPath("$[0].contents").value("contents1"))
                .andExpect(jsonPath("$[0].user.id").value(user.getId()))
                .andExpect(jsonPath("$[0].user.email").value(user.getEmail()));
    }
}