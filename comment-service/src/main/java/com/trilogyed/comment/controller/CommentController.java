package com.trilogyed.comment.controller;

import com.trilogyed.comment.dao.CommentDao;
import com.trilogyed.comment.exception.NotFoundException;
import com.trilogyed.comment.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentDao commentDao;

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Comment createComment(@RequestBody Comment comment) {
        commentDao.createComment(comment);
        return comment;
    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Comment getComment(@PathVariable Integer id) {
        Comment comment = commentDao.getComment(id);
        if (comment == null)
            throw new NotFoundException("Comment does not exist.");
        return comment;
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Comment> getAllComments() {
        return commentDao.getAllComments();
    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateComment(@RequestBody Comment comment, @PathVariable Integer id) {
        Comment exists = commentDao.getComment(comment.getCommentId());
        if (exists == null)
            throw new IllegalArgumentException("Comment " + id + " does not exist. Cannot be updated.");
        commentDao.updateComment(comment);

    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer id) {
        commentDao.deleteComment(id);
    }

    @RequestMapping(value = "/comments/post/{postId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Comment> getAllCommentsByPostId(@PathVariable(name = "postId") Integer postId) {
        return commentDao.getAllCommentsByPostId(postId);
    }

    @RequestMapping(value = "/comments/name/{commenterName}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Comment> getAllCommentsByName(@PathVariable(name = "commenterName") String commenterName) {
        return commentDao.getAllCommentsByName(commenterName);
    }
}

