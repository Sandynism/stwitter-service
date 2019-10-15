package com.trilogyed.post.controller;

import com.trilogyed.post.dao.PostDao;
import com.trilogyed.post.exception.NotFoundException;
import com.trilogyed.post.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    PostDao postDao;

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post) {
        postDao.createPost(post);
        return post;
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Post getPost(@PathVariable Integer id) {
        Post post = postDao.getPost(id);
        if (post == null)
            throw new NotFoundException("Post does not exist.");
        return post;
    }

    @RequestMapping(value="/posts", method= RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    public List<Post> getAllPosts() {
        return postDao.getAllPosts();
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePost(@RequestBody Post post, @PathVariable Integer id) {
        Post exists = postDao.getPost(post.getPostId());
        if (exists == null)
            throw new IllegalArgumentException("Post " + id + " does not exist. Cannot be updated.");
        postDao.updatePost(post);
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Integer id) {
        postDao.deletePost(id);
    }

    @RequestMapping(value="/posts/user/{posterName}", method = RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    public List<Post> getAllPostsByName(@PathVariable(name ="posterName") String posterName) {
        return postDao.getAllPostsByName(posterName);
    }
}
