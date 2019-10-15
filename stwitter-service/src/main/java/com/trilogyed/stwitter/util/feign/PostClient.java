package com.trilogyed.stwitter.util.feign;

import com.trilogyed.stwitter.domain.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "post-service")
public interface PostClient {

    @PostMapping(value = "/posts")
    Post createPost(@RequestBody Post post);

    @GetMapping(value = "/posts/{id}")
    Post getPost(@PathVariable int id);

    @PutMapping(value = "/posts/{id}")
    void updatePost(@RequestBody Post post, @PathVariable int id);

    @DeleteMapping(value = "/posts/{id}")
    void deletePost(@PathVariable int id);

    @GetMapping(value = "/posts/user/{posterName}")
    List<Post> getAllPostsByName(@PathVariable String posterName);

    @GetMapping(value = "/posts")
    List<Post> getAllPosts();

}
