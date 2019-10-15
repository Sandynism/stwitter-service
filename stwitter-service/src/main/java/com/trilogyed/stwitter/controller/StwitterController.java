package com.trilogyed.stwitter.controller;

import com.trilogyed.stwitter.domain.CommentViewModel;
import com.trilogyed.stwitter.domain.Post;
import com.trilogyed.stwitter.domain.PostViewModel;
import com.trilogyed.stwitter.exception.NoSuchCommentException;
import com.trilogyed.stwitter.exception.NoSuchPostException;
import com.trilogyed.stwitter.exception.NotFoundException;
import com.trilogyed.stwitter.service.ServiceLayer;
import com.trilogyed.stwitter.util.messages.Comment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"comments"})
public class StwitterController {

    public static final String EXCHANGE_NAME = "comment-exchange";
    public static final String ROUTING_KEY = "comment.create.swtitter.controller";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ServiceLayer service;

    public StwitterController(RabbitTemplate rabbitTemplate, ServiceLayer service) {
        this.rabbitTemplate = rabbitTemplate;
        this.service = service;
    }

    //    POST
    @PostMapping(value = "/posts")
    @ResponseStatus(value = HttpStatus.CREATED)
    public PostViewModel createPost(@RequestBody PostViewModel pvm) {
//        PostViewModel exists = service.getPost(pvm.getPostId());
//        if (exists != null)
//            throw new IllegalArgumentException("POST " + pvm.getPostId() + " ALREADY EXISTS!");
        service.createPost(pvm);
        return pvm;
    }

    @GetMapping(value = "/posts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PostViewModel getPostById(@PathVariable Integer id) {
        PostViewModel post = service.getPost(id);
        if (post == null)
            throw new NoSuchPostException(id);
        return post;
    }

    @GetMapping(value = "/posts/user/{posterName}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<PostViewModel> getPostsByPosterName(@PathVariable(name = "posterName") String posterName) {
        List<PostViewModel> pvmList = service.getAllPostsByName(posterName);
        if (pvmList != null && pvmList.size() == 0)
            throw new NotFoundException("Posts by " + posterName + " could not be found.");
        return pvmList;
    }

    @GetMapping(value = "/posts")
    @ResponseStatus(value = HttpStatus.OK)
    public List<PostViewModel> getAllPosts() {
        return service.getAllPosts();
    }

    @DeleteMapping(value = "/posts/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Integer id) {
        service.deletePost(id);
    }

    @PutMapping(value = "/posts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePost(@RequestBody PostViewModel pvm, @PathVariable Integer id) {
        PostViewModel post = service.getPost(id);
        if (post == null)
            throw new NoSuchPostException(id);
        service.updatePost(pvm);
    }


    //    COMMENT
    @CachePut(key = "#result.getCommentId()")
    @PostMapping(value = "/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentViewModel createComment(@RequestBody CommentViewModel cvm) {
//        CommentViewModel exists = service.getComment(cvm.getCommentId());
//        if (exists != null)
//            throw new IllegalArgumentException("Comment " + cvm.getCommentId() + " already exists!");
        service.createComment(cvm);

        Comment msg = new Comment(cvm.getCommentId(), cvm.getPostId(), cvm.getCreateDate(), cvm.getCommenterName(), cvm.getComment());
        System.out.println("CREATING COMMENT ID " + cvm.getCommentId());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, msg);

        return cvm;
    }

    @Cacheable
    @GetMapping(value = "/comments/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public CommentViewModel getCommentById(@PathVariable Integer id) {
        CommentViewModel comment = service.getComment(id);
        if (comment == null)
            throw new NoSuchCommentException(id);

        System.out.println("GETTING COMMENT ID: " + id);
        return comment;
    }

    @GetMapping(value = "/comments")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CommentViewModel> getAllComments() {
        return service.getAllComments();
    }

    @GetMapping(value = "/comments/name/{commenterName}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CommentViewModel> getAllCommentsByName(@PathVariable(name = "commenterName") String commenterName) {
        List<CommentViewModel> cvmList = service.getAllCommentsByName(commenterName);
        if (cvmList != null && cvmList.size() == 0)
            throw new NotFoundException("Comments by " + commenterName + " could not be found.");
        return cvmList;
    }

    @GetMapping(value = "/comments/post/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CommentViewModel> getAllCommentsByPostId(@PathVariable(name = "postId") Integer postId) {
        List<CommentViewModel> cvmList = service.getAllCommentsByPostId(postId);
        if (cvmList != null && cvmList.size() == 0)
            throw new NotFoundException("Comments by " + postId + " could not be found.");
        return cvmList;
    }

    @CacheEvict(key="#cvm.getCommentId()")
    @PutMapping(value = "/comments/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateComment(@RequestBody CommentViewModel cvm, @PathVariable Integer id) {
        CommentViewModel comment = service.getComment(id);
        if (comment == null)
            throw new NoSuchCommentException(id);
        System.out.println("UPDATING COMMENT ID: " + id );
        service.updateComment(cvm);
    }

    @CacheEvict
    @DeleteMapping(value = "/comments/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer id) {
        System.out.println("DELETING COMMENT " + id);
        service.deleteComment(id);
    }
}

