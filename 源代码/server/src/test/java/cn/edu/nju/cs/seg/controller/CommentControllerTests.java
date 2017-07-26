package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by fwz on 2017/7/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/mvc-test-config.xml"})
@WebAppConfiguration
public class CommentControllerTests {

    private static final int TEST_COMMENT_ID = 1;

    @Autowired
    private CommentController commentController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        Random random = new Random(System.currentTimeMillis());
        String name = "";
        for (int i = 0; i < 6; i++) {
            name += random.nextInt(10);
        }
        User user = new User(name + "@nju.edu.cn", "1234");
        Studio studio = new Studio(name, user);
        Question question = new Question(
                "title", "content", user, studio, Question.TYPE_TEXT);
        Answer answer = new Answer("content", user, question, Answer.TYPE_TEXT);
//        Essay essay = new Essay("title", "content", studio, Essay.TYPE_TEXT);
        UserService.add(user);
        StudioService.add(studio);
        StudioService.addMember(studio.getId(), user.getId());
        QuestionService.add(question);
        AnswerService.add(answer);
        Comment comment = new Comment(user, "content", answer);
        CommentService.add(comment);
//        EssayService.add(essay);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(commentController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
    }

    @Test
    public void testGetOneCommentSuccess() throws Exception {
        List<Comment> comments = CommentService.findAllComments();
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/comments/" + comments.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comments.get(0).getId()))
                .andExpect(jsonPath("$.commenter").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.url").exists())
                .andExpect(jsonPath("$.commenter_url").exists())
                .andExpect(jsonPath("$.commenter_avatar_url").exists())
                .andExpect(jsonPath("$.parent_url").exists())
                .andExpect(jsonPath("$.created_at").exists());
    }

    @Test
    public void testGetOneCommentNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostOneComment() throws Exception {
        List<User> users = UserService.findAllUsers();
        List<Answer> answers = AnswerService.findAllAnswers();
        String requestBody = new JsonMapBuilder()
                .append("commenter_email_or_phone", users.get(0).getEmail())
                .append("commenter_password", users.get(0).getPassword())
                .append("content", "content")
                .append("parent_type", "answer")
                .append("parent_id", answers.get(0).getId())
                .toString();

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/comments")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteOneComment() throws Exception {
        List<Comment> comments = CommentService.findAllComments();
        Comment comment = comments.get(0);
        String requestBody = new JsonMapBuilder()
                .append("commenter_email_or_phone", comment.getUser().getEmail())
                .append("commenter_password", comment.getUser().getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/comments/" + comment.getId())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}
