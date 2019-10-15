package com.trilogyed.post.dao;

import com.trilogyed.post.model.Post;
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
public class PostDaoTest {

    @Autowired
    PostDao postDao;

    @Before
    public void setUp() throws Exception {
        List<Post> postList = postDao.getAllPosts();
        postList.stream().forEach(p -> postDao.deletePost(p.getPostId()));
    }

    @Test
    public void createGetDeletePost() {
        Post post = new Post();
        post.setPostDate(LocalDate.of(2019, 9, 26));
        post.setPosterName("Sandy");
        post.setPost("First tweet.");

        post = postDao.createPost(post);

        Post post2 = postDao.getPost(post.getPostId());

        assertEquals(post, post2);

        postDao.deletePost(post.getPostId());

        post2 = postDao.getPost(post.getPostId());

        assertNull(post2);
    }

    @Test
    public void getAllPosts() {
        Post post = new Post();
        post.setPostDate(LocalDate.of(2019, 9, 26));
        post.setPosterName("Sandy");
        post.setPost("First tweet.");

        postDao.createPost(post);

        Post post2 = new Post();
        post2.setPostDate(LocalDate.of(2019, 9, 27));
        post2.setPosterName("Kenny");
        post2.setPost("Tweet");

        postDao.createPost(post2);

        List<Post> postList = postDao.getAllPosts();
        assertEquals(postList.size(), 2);
    }

    @Test
    public void updatePost() {
        Post post = new Post();
        post.setPostDate(LocalDate.of(2019, 9, 26));
        post.setPosterName("Sandy");
        post.setPost("First tweet");

        postDao.createPost(post);

        post.setPost("First tweet.");

        postDao.updatePost(post);

        Post updatedPost = postDao.getPost(post.getPostId());
        assertEquals(post, updatedPost);
    }

    @Test
    public void getAllPostsByName() {
        Post post = new Post();
        post.setPostDate(LocalDate.of(2019, 9, 26));
        post.setPosterName("Sandy");
        post.setPost("First tweet.");

        postDao.createPost(post);

        Post post2 = new Post();
        post2.setPostDate(LocalDate.of(2019, 9, 27));
        post2.setPosterName("Kenny");
        post2.setPost("Tweet");

        postDao.createPost(post2);

        List<Post> sandyList = postDao.getAllPostsByName("Sandy");
        assertEquals(sandyList.size(), 1);

        List<Post> notExistList = postDao.getAllPostsByName("Patrick");
        assertEquals(notExistList.size(), 0);
    }
}