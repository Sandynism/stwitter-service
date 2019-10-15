package com.trilogyed.comment.dao;

import com.trilogyed.comment.model.Comment;

import java.util.List;

public interface CommentDao {
    Comment createComment(Comment comment);

    Comment getComment(int commentId);

    List<Comment> getAllComments();

    void updateComment(Comment comment);

    void deleteComment(int commentId);

    List<Comment> getAllCommentsByName(String commenterName);

    List<Comment> getAllCommentsByPostId(int postId);

}
