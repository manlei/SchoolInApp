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
public class StudioControllerTests {
    private static final int TEST_STUDIO_ID = 1;

    @Autowired
    private StudioController studioController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        Random random = new Random(System.currentTimeMillis());
        String name = "";
        for (int i = 0; i < 6; i++) {
            name += random.nextInt(10);
        }
        User user = new User(name + "@nju.edu.cn", "1234");
        User user2 = new User(name + "@smail.nju.edu.cn", "1234");
        Studio studio = new Studio(name, user);
        Question question = new Question(
                "title", "content", user, studio, Question.TYPE_TEXT);
        Answer answer = new Answer("content", user, question, Answer.TYPE_TEXT);
        Essay essay = new Essay("title", "content", studio, Essay.TYPE_TEXT);
        UserService.add(user);
        UserService.add(user2);
        StudioService.add(studio);
        StudioService.addMember(studio.getId(), user.getId());
        StudioService.addMember(studio.getId(), user2.getId());
        QuestionService.add(question);
        AnswerService.add(answer);
        EssayService.add(essay);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(studioController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
    }

    @Test
    public void testGetStudiosSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/studios"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneStudioSuccess() throws Exception {
        List<Studio> studios = StudioService.findAllStudios();
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/studios/" + studios.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studios.get(0).getId()))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.manager").exists())
                .andExpect(jsonPath("$.members").exists())
                .andExpect(jsonPath("$.questions").exists())
                .andExpect(jsonPath("$.essays").exists())
                .andExpect(jsonPath("$.url").exists())
                .andExpect(jsonPath("$.avatar_url").exists())
                .andExpect(jsonPath("$.manager_url").exists())
                .andExpect(jsonPath("$.manager_avatar_url").exists())
                .andExpect(jsonPath("$.members_url").exists())
                .andExpect(jsonPath("$.questions_url").exists())
                .andExpect(jsonPath("$.essays_url").exists())
                .andExpect(jsonPath("$.top_question_url").exists())
                .andExpect(jsonPath("$.top_essay_url").exists())
                .andExpect(jsonPath("$.bio").exists());
    }

    @Test
    public void testGetOneStudioNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetStudioMembersSuccess() throws Exception {
        List<Studio> studios = StudioService.findAllStudios();
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/studios/" + studios.get(0).getId() + "/members"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetStudioQuestionsSuccess() throws Exception {
        List<Studio> studios = StudioService.findAllStudios();

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/studios/" + studios.get(0).getId() + "/questions"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetStudioEssaysSuccess() throws Exception {
        List<Studio> studios = StudioService.findAllStudios();
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/studios/" + studios.get(0).getId() + "/essays"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostOneStudioSuccess() throws Exception {
        User user = UserService.findUserById(1);
        String requestBody = new JsonMapBuilder()
                .append("name", "Test")
                .append("manager_email_or_phone", user.getEmail())
                .append("manager_password", user.getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/studios")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("url").exists());
    }

    @Test
    public void testPostOneStudioApplicationSuccess() throws Exception {
        User user = UserService.findUserById(1);
        String requestBody = new JsonMapBuilder()
                .append("name", "Test")
                .append("manager_email_or_phone", user.getEmail())
                .append("manager_password", user.getPassword())
                .append("qualification", "qualification")
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/studios/application")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isAccepted());
//                .andExpect(jsonPath("url").exists());
    }

    @Test
    public void testPostOneStudioMembersSuccess() throws Exception {
        List<Studio> studios = StudioService.findAllStudios();
        List<User> users = UserService.findAllUsers();
        Studio studio = studios.get(0);
        User user = users.get(0);
        String requestBody = new JsonMapBuilder()
                .append("manager_email_or_phone", studio.getManager().getEmail())
                .append("manager_password", studio.getManager().getPassword())
                .append("member_email_or_phone", user.getEmail())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/studios/"+ studio.getId() + "/members")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("url").exists());
    }

    @Test
    public void testPutOneStudio() throws Exception {
        List<Studio> studios = StudioService.findAllStudios();
        Studio studio = studios.get(0);
        List<User> members = StudioService.findUsersByStudioId(studio.getId());
        String requestBody = new JsonMapBuilder()
                .append("name", "test")
                .append("old_manager_email_or_phone", studio.getManager().getEmail())
                .append("old_manager_password", studio.getManager().getPassword())
                .append("new_manager_email_or_phone", members.get(1).getEmail())
                .append("new_manager_password", members.get(1).getPassword())
                .append("bio", "bio")
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/api/studios/" + studio.getId())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/studios/" + studio.getId()))
                .andExpect(jsonPath("name").value("test"))
                .andExpect(jsonPath("bio").value("bio"));
    }


    @Test
    public void testDeleteOneStudio() throws Exception {
        Studio studio = StudioService.findStudioById(TEST_STUDIO_ID);
        String requestBody = new JsonMapBuilder()
                .append("manager_email_or_phone", studio.getManager().getEmail())
                .append("manager_password", studio.getManager().getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/studios/"+ TEST_STUDIO_ID)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}
