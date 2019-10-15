package com.trilogyed.stwitter.util.feign;

import com.trilogyed.stwitter.domain.Comment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "comment-service")
public interface CommentClient {

    @PostMapping(value = "/comments")
    Comment createComment(@RequestBody Comment comment);

    @GetMapping(value = "/comments/{id}")
    Comment getComment(@PathVariable int id);

    @PutMapping(value = "/comments/{id}")
    void updateComment(@RequestBody Comment comment, @PathVariable int id);

    @DeleteMapping(value = "/comments/{id}")
    void deleteComment(@PathVariable int id);

    @GetMapping(value = "/comments")
    List<Comment> getAllComments();

    @GetMapping(value = "/comments/post/{postId}")
    List<Comment> getAllCommentsByPostId(@PathVariable(name = "postId") int postId);

    @GetMapping(value = "/comments/name/{commenterName}")
    List<Comment> getAllCommentsByName(@PathVariable String commenterName);
}
