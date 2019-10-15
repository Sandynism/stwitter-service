package com.trilogyed.stwitter.service;

import com.trilogyed.stwitter.domain.Comment;
import com.trilogyed.stwitter.domain.CommentViewModel;
import com.trilogyed.stwitter.domain.Post;
import com.trilogyed.stwitter.domain.PostViewModel;
import com.trilogyed.stwitter.util.feign.CommentClient;
import com.trilogyed.stwitter.util.feign.PostClient;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ServiceLayerTest {

    ServiceLayer serviceLayer;
    PostClient pc;
    CommentClient cc;

    private static final Integer COMMENT_ID = new Integer(1);
    private static final Integer POST_ID = new Integer(1);
    private static final LocalDate CREATE_DATE = LocalDate.of(2019, 9, 27);
    private static final String COMMENTER_NAME = "Heather";
    private static final String COMMENT = "Check out my twitter account!";
    private static final List<String> STRING_COMMENTS = new ArrayList<>(Arrays.asList("Check out my twitter account!"));
    private static final String POST = "First tweet.";
    private static final LocalDate POST_DATE = LocalDate.of(2019, 9, 26);
    private static final String POSTER_NAME = "Daniel";

    @Before
    public void setUp() throws Exception {
        setUpPostClientMocks();
        setUpCommentClientMocks();
        serviceLayer = new ServiceLayer(pc, cc);
    }

    private void setUpPostClientMocks() {
        pc = mock(PostClient.class);

        Post post = new Post();
        post.setPostId(POST_ID);
        post.setPost(POST);
        post.setPostDate(POST_DATE);
        post.setPosterName(POSTER_NAME);
        post.setComments(STRING_COMMENTS);

        Post post1 = new Post();
        post1.setPost(POST);
        post1.setPostDate(POST_DATE);
        post1.setPosterName(POSTER_NAME);
        post1.setComments(STRING_COMMENTS);

        List<Post> postList = new ArrayList<>();
        postList.add(post);

        doReturn(post).when(pc).createPost(post1);
        doReturn(post).when(pc).getPost(POST_ID);
        doReturn(postList).when(pc).getAllPosts();

        Post post2 = new Post();
        post2.setPostId(2);
        post2.setPost(POST + "updated");
        post2.setPostDate(POST_DATE);
        post2.setPosterName(POSTER_NAME);
        post2.setComments(STRING_COMMENTS);

//        Post post3 = new Post();
//        post3.setPostId(3);
//        post3.setPost(POST);
//        post3.setPostDate(POST_DATE);
//        post3.setPosterName(POSTER_NAME);
//        post3.setComments(STRING_COMMENTS);
//
//        postList.add(post3);

        List<Post> nameList = new ArrayList<>();
        nameList.add(post);

        doReturn(nameList).when(pc).getAllPostsByName(POSTER_NAME);
        doNothing().when(pc).updatePost(post2, 2);
        doReturn(post2).when(pc).getPost(2);
        doNothing().when(pc).deletePost(3);
        doReturn(null).when(pc).getPost(3);
    }

    private void setUpCommentClientMocks() {
        cc = mock(CommentClient.class);

        Comment comment = new Comment();
        comment.setCommentId(COMMENT_ID);
        comment.setPostId(POST_ID);
        comment.setCreateDate(CREATE_DATE);
        comment.setCommenterName(COMMENTER_NAME);
        comment.setComment(COMMENT);

        Comment comment1 = new Comment();
        comment1.setPostId(POST_ID);
        comment1.setCreateDate(CREATE_DATE);
        comment1.setCommenterName(COMMENTER_NAME);
        comment1.setComment(COMMENT);

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

        doReturn(comment).when(cc).createComment(comment1);
        doReturn(comment).when(cc).getComment(COMMENT_ID);
        doReturn(commentList).when(cc).getAllComments();
        doReturn(commentList).when(cc).getAllCommentsByName("Heather");
        doReturn(commentList).when(cc).getAllCommentsByPostId(POST_ID);
        doReturn(commentList).when(cc).getAllCommentsByPostId(2);

        Comment comment2 = new Comment();
        comment2.setCommentId(2);
        comment2.setPostId(POST_ID);
        comment2.setCreateDate(CREATE_DATE);
        comment2.setCommenterName(COMMENTER_NAME);
        comment2.setComment(COMMENT + "updated");
//
//        Comment comment3 = new Comment();
//        comment3.setCommentId(3);
//        comment3.setPostId(POST_ID);
//        comment3.setCreateDate(CREATE_DATE);
//        comment3.setCommenterName(COMMENTER_NAME);
//        comment3.setComment(COMMENT);
//
//        commentList.add(comment3);

//        doNothing().when(cc).updateComment(comment2, 2);
//        doReturn(comment2).when(cc).getComment(2);
//        doNothing().when(cc).deleteComment(3);
//        doReturn(null).when(cc).getComment(3);
    }

    @Test
    public void createGetUpdateDeleteGetAllPost() {

        PostViewModel pvm = new PostViewModel();
        pvm.setPost(POST);
        pvm.setPostDate(POST_DATE);
        pvm.setPosterName(POSTER_NAME);
        pvm.setComments(STRING_COMMENTS);

        List<PostViewModel> allList = new ArrayList<>();
        allList.add(pvm);

        pvm = serviceLayer.createPost(pvm);
        PostViewModel fromService = serviceLayer.getPost(pvm.getPostId());
        assertEquals(pvm, fromService);

        pvm = new PostViewModel();

        Post post2 = new Post();
        post2.setPostId(2);
        post2.setPost(POST + "updated");
        post2.setPostDate(POST_DATE);
        post2.setPosterName(POSTER_NAME);
        post2.setComments(STRING_COMMENTS);

        pvm.setPostId(post2.getPostId());
        pvm.setPost(post2.getPost());
        pvm.setPostDate(post2.getPostDate());
        pvm.setPosterName(post2.getPosterName());
        pvm.setComments(post2.getComments());

        serviceLayer.updatePost(pvm);

        fromService = serviceLayer.getPost(2);

        List<PostViewModel> pvmList = serviceLayer.getAllPosts();
        assertEquals(allList, pvmList);

//        Post post3 = new Post();
//        post3.setPostId(fromService.getPostId());
//        post3.setPost(fromService.getPost());
//        post3.setPostDate(fromService.getPostDate());
//        post3.setPosterName(fromService.getPosterName());
//        post3.setComments(fromService.getComments());
//
//        assertEquals(post2, post3);
//
//        serviceLayer.deletePost(3);
//        fromService = serviceLayer.getPost(3);
//        assertNull(fromService);
    }

    @Test
    public void getAllPostsByName() {

        PostViewModel pvm = new PostViewModel();
        pvm.setPost(POST);
        pvm.setPostDate(POST_DATE);
        pvm.setPosterName(POSTER_NAME);
        pvm.setComments(STRING_COMMENTS);

        pvm = serviceLayer.createPost(pvm);
        List<PostViewModel> fromService = serviceLayer.getAllPostsByName(POSTER_NAME);
        assertEquals(fromService.size(), 1);

        fromService = serviceLayer.getAllPostsByName("Jesus");
        assertEquals(fromService.size(), 0);
    }

    @Test
    public void createGetUpdateDeleteGetAllComment() {
        CommentViewModel cvm = new CommentViewModel();
        cvm.setPostId(POST_ID);
        cvm.setCreateDate(CREATE_DATE);
        cvm.setCommenterName(COMMENTER_NAME);
        cvm.setComment(COMMENT);

        List<CommentViewModel> allList = new ArrayList<>();
        allList.add(cvm);

        cvm = serviceLayer.createComment(cvm);
        CommentViewModel fromService = serviceLayer.getComment(cvm.getCommentId());
        assertEquals(cvm, fromService);

        List<CommentViewModel> cvmList = serviceLayer.getAllComments();
        assertEquals(allList, cvmList);
    }

    @Test
    public void getAllCommentsByName() {
        CommentViewModel cvm = new CommentViewModel();
        cvm.setPostId(POST_ID);
        cvm.setCreateDate(CREATE_DATE);
        cvm.setCommenterName(COMMENTER_NAME);
        cvm.setComment(COMMENT);

        cvm = serviceLayer.createComment(cvm);
        List<CommentViewModel> fromService = serviceLayer.getAllCommentsByName(COMMENTER_NAME);
        assertEquals(fromService.size(), 1);

        fromService = serviceLayer.getAllCommentsByName("Mary");
        assertEquals(fromService.size(), 0);
    }

    @Test
    public void getAllCommentsByPostId() {
        CommentViewModel cvm = new CommentViewModel();
        cvm.setPostId(POST_ID);
        cvm.setCreateDate(CREATE_DATE);
        cvm.setCommenterName(COMMENTER_NAME);
        cvm.setComment(COMMENT);

        cvm = serviceLayer.createComment(cvm);
        List<CommentViewModel> fromService = serviceLayer.getAllCommentsByPostId(POST_ID);
        assertEquals(fromService.size(), 1);

        fromService = serviceLayer.getAllCommentsByPostId(10);
        assertEquals(fromService.size(), 0);
    }
}