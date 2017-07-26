package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Comment;

import java.util.List;

public interface CommentDao {

    public Comment findCommentById(int id);

    public Comment add(Comment comment);

    public boolean remove(int Id);

    public List<Comment> findAllComments();

    public List<Comment> findCommentsByEssayId(int essayId);

    public List<Comment> findCommentsByAnswerId(int answerId);
}