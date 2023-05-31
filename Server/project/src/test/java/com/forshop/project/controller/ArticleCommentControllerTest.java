package com.forshop.project.controller;

import com.forshop.project.config.TestSecurityConfig;
import com.forshop.project.dto.ArticleCommentDto;
import com.forshop.project.dto.request.ArticleCommentRequest;
import com.forshop.project.dto.security.ServicePrincipal;
import com.forshop.project.service.ArticleCommentService;
import com.forshop.project.service.UserAccountService;
import com.forshop.project.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("댓글 컨트롤러")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@ActiveProfiles("test")
@WebMvcTest(ArticleCommentController.class)
public class ArticleCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleCommentService articleCommentService;
    @MockBean
    private UserAccountService userAccountService;

    private FormDataEncoder formDataEncoder;
    @Mock
    private Principal principal;


    ArticleCommentControllerTest(
            @Autowired MockMvc mockMvc,
            @Autowired ArticleCommentService articleCommentService
    ) {
        this.mockMvc = mockMvc;
        this.articleCommentService = articleCommentService;
    }

    @Test
    @WithUserDetails(value = "testId", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST] 댓글 등록 - 정상 호출")
    public void createArticleCommentTest() throws Exception {

        long articleId = 1L;
        ArticleCommentRequest request = ArticleCommentRequest.of(articleId, "testComment");
        willDoNothing().given(articleCommentService).saveArticleComment(any(ArticleCommentDto.class));

        mockMvc.perform(post("/api/comments/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(formDataEncoder.encode(request))
                    )
                .andExpect(status().isOk())
                .andExpect(content().string("댓글 정보가 저장되었습니다."));
        then(articleCommentService).should().saveArticleComment(any(ArticleCommentDto.class));
    }

//    @Test
//    public void updateArticleCommentTest() throws Exception {
//        doNothing().when(articleCommentService).updateArticleComment(any());
//
//        mockMvc.perform(put("/api/comments/{commentId}", 1L)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonConvertor.asJsonString(articleCommentRequest))
//                .principal(servicePrincipal))
//                .andExpect(status().isOk())
//                .andExpect(content().string("댓글 정보가 수정되었습니다."));
//    }
//
//    @Test
//    public void deleteArticleCommentTest() throws Exception {
//        doNothing().when(articleCommentService).deleteArticleComment(anyLong(), anyString());
//
//        mockMvc.perform(delete("/api/comments/{commentId}", 1L)
//                .principal(servicePrincipal))
//                .andExpect(status().isOk())
//                .andExpect(content().string("댓글 정보가 삭제되었습니다."));
//    }
}
