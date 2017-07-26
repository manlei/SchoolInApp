package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.exception.BusinessException;
import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.json.JsonMapResponseBuilderFactory;
import cn.edu.nju.cs.seg.pojo.Answer;
import cn.edu.nju.cs.seg.pojo.Comment;
import cn.edu.nju.cs.seg.pojo.Essay;
import cn.edu.nju.cs.seg.pojo.User;
import cn.edu.nju.cs.seg.service.AnswerService;
import cn.edu.nju.cs.seg.service.CommentService;
import cn.edu.nju.cs.seg.service.EssayService;
import cn.edu.nju.cs.seg.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Clypso on 2017/5/5.
 */
@RestController
@RequestMapping("/api")
public class CommentController {
    private Logger LOG = LoggerFactory.getLogger(AnswerController.class);


    @RequestMapping(value = "/comments/{commentId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getComment(
            @PathVariable("commentId") int commentId) {
        Comment comment = CommentService.findCommentById(commentId);
        Map<String, Object> m;
        if (comment != null) {
            m = JsonMapResponseBuilderFactory
                    .createCommentJsonMapBuilder(comment)
                    .getComplexMap();
//            System.out.println("comment");

            return new ResponseEntity<Map<String, Object>>(m, HttpStatus.OK);
        } else {
            throw new BusinessException("Comment not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postComment(
            @RequestBody Map<String, Object> requestBody) throws Exception {
        String commenterEmailOrPhone = (String) requestBody.get("commenter_email_or_phone");
        String commenterPassword = (String) requestBody.get("commenter_password");
        String content = (String) requestBody.get("content");
        String parentType = (String) requestBody.get("parent_type");
        User commenter = UserService.findUserByEmailOrPhone(commenterEmailOrPhone);
        if (commenter != null
                && commenterPassword != null
                && commenterPassword.equals(commenter.getPassword())) {
            if (content != null && parentType != null && requestBody.get("parent_id") != null) {
                int parentId = (int) requestBody.get("parent_id");
                int commentId;
                if (parentType.equals("answer")) {
                    Answer answer = AnswerService.findAnswerById(parentId);
                    System.out.println(parentId);
                    if (answer == null) {
                        throw new BusinessException("answer not found", HttpStatus.NOT_FOUND);
                    }
                    Comment comment = new Comment(commenter, content, answer);
                    System.out.println(comment.getAnswer().getAnswerer().getUsername());
                    commentId = CommentService.add(comment);

                } else if (parentType.equals("essay")) {
                    Essay essay = EssayService.findEssayById(parentId);
                    if (essay == null) {
                        throw new BusinessException("answer not found", HttpStatus.NOT_FOUND);
                    }
                    Comment comment = new Comment(commenter, content, essay);
                    commentId = CommentService.add(comment);

                } else {
                    throw new BusinessException("Invalid parent type", HttpStatus.BAD_REQUEST);
                }
                Map<String, Object> map = new JsonMapBuilder()
                        .append("url", ServerConfig.SERVER_BASE_URL + "/comments/" + commentId)
                        .getMap();
                return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);

            } else {
                throw new BusinessException("Bad request", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new BusinessException("Invalid user or password", HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(value = "/comments/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable("commentId") int commentId,
            @RequestBody Map<String, Object> requestBody) {
        String commenterEmailOrPhone = (String) requestBody.get("commenter_email_or_phone");
        String commenterPassword = (String) requestBody.get("commenter_password");
        Comment comment = CommentService.findCommentById(commentId);
        if (comment != null) {
            User commenter = UserService.findUserByEmailOrPhone(commenterEmailOrPhone);
            if (commenter != null
                    && commenterPassword != null
                    && commenterPassword.equals(commenter.getPassword())) {
                CommentService.remove(commentId);
                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
            } else {
                throw new BusinessException("Invalid user or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Comment not found", HttpStatus.NOT_FOUND);
        }
    }


}