package com.trilogyed.post.dao;

import com.trilogyed.post.model.Post;

import java.util.List;

public interface PostDao {
    Post createPost(Post post);

    Post getPost(int postId);

    List<Post> getAllPosts();

    void updatePost(Post post);

    void deletePost(int postId);

    List<Post> getAllPostsByName(String posterName);
}
