package com.trilogyed.comment.dao;

import com.trilogyed.comment.model.Comment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CommentDaoTest {

    @Autowired
    CommentDao commentDao;

    @Before
    public void setUp() throws Exception {
        List<Comment> commentList = commentDao.getAllComments();
        commentList.stream().forEach(c -> commentDao.deleteComment(c.getCommentId()));
    }

    @Test
    public void createGetDeleteComment() {
        Comment comment = new Comment();
        comment.setPostId(1);
        comment.setCreateDate(LocalDate.of(2019, 9, 26));
        comment.setCommenterName("George");
        comment.setComment("Welcome to Stwitter!");

        comment = commentDao.createComment(comment);

        Comment comment2 = commentDao.getComment(comment.getCommentId());

        assertEquals(comment, comment2);

        commentDao.deleteComment(comment.getCommentId());

        comment2 = commentDao.getComment(comment.getCommentId());

        assertNull(comment2);
    }

    @Test
    public void getAllComments() {
        Comment comment = new Comment();
        comment.setPostId(1);
        comment.setCreateDate(LocalDate.of(2019, 9, 26));
        comment.setCommenterName("George");
        comment.setComment("Welcome to Stwitter!");

        commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setPostId(2);
        comment2.setCreateDate(LocalDate.of(2019, 9, 28));
        comment2.setCommenterName("Meghan");
        comment2.setComment("Chirp chirp");

        commentDao.createComment(comment2);

        List<Comment> commentsList = commentDao.getAllComments();
        assertEquals(commentsList.size(), 2);
    }

    @Test
    public void updateComment() {
        Comment comment = new Comment();
        comment.setPostId(1);
        comment.setCreateDate(LocalDate.of(2019, 9, 26));
        comment.setCommenterName("George");
        comment.setComment("Welcome to Stwitter!");

        commentDao.createComment(comment);

        comment.setComment("Welcome to Stwitter!!");

        commentDao.updateComment(comment);

        Comment updatedComment = commentDao.getComment(comment.getCommentId());
        assertEquals(comment, updatedComment);
    }


    @Test
    public void getAllCommentsByName() {
        Comment comment = new Comment();
        comment.setPostId(1);
        comment.setCreateDate(LocalDate.of(2019, 9, 26));
        comment.setCommenterName("George");
        comment.setComment("Welcome to Stwitter!");

        commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setPostId(2);
        comment2.setCreateDate(LocalDate.of(2019, 9, 28));
        comment2.setCommenterName("Meghan");
        comment2.setComment("Chirp chirp");

        commentDao.createComment(comment2);

        List<Comment> meghanList = commentDao.getAllCommentsByName("Meghan");
        assertEquals(meghanList.size(), 1);

        List<Comment> notExistList = commentDao.getAllCommentsByName("Yoda");
        assertEquals(notExistList.size(), 0);
    }

    @Test
    public void getAllCommentsByPostId() {
        Comment comment = new Comment();
        comment.setPostId(1);
        comment.setCreateDate(LocalDate.of(2019, 9, 26));
        comment.setCommenterName("George");
        comment.setComment("Welcome to Stwitter!");

        commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setPostId(2);
        comment2.setCreateDate(LocalDate.of(2019, 9, 28));
        comment2.setCommenterName("Meghan");
        comment2.setComment("Chirp chirp");

        commentDao.createComment(comment2);

        List<Comment> postList = commentDao.getAllCommentsByPostId(1);
        assertEquals(postList.size(), 1);

        List<Comment> emptyList = commentDao.getAllCommentsByPostId(5);
        assertEquals(emptyList.size(), 0);
    }
}