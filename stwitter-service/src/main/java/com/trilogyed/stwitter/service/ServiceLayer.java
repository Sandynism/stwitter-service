package com.trilogyed.stwitter.service;

import com.trilogyed.stwitter.domain.Comment;
import com.trilogyed.stwitter.domain.CommentViewModel;
import com.trilogyed.stwitter.domain.Post;
import com.trilogyed.stwitter.domain.PostViewModel;
import com.trilogyed.stwitter.exception.NoSuchCommentException;
import com.trilogyed.stwitter.exception.NoSuchPostException;
import com.trilogyed.stwitter.exception.NotFoundException;
import com.trilogyed.stwitter.util.feign.CommentClient;
import com.trilogyed.stwitter.util.feign.PostClient;
import feign.Feign;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {

    PostClient postClient;
    CommentClient commentClient;

    @Autowired
    public ServiceLayer(PostClient postClient, CommentClient commentClient) {
        this.postClient = postClient;
        this.commentClient = commentClient;
    }

    public PostViewModel createPost(PostViewModel pvm) {
        Post post = new Post();
        post.setPost(pvm.getPost());
        post.setPostDate(pvm.getPostDate());
        post.setPosterName(pvm.getPosterName());
        post.setComments(pvm.getComments());

        post = postClient.createPost(post);

        pvm.setPostId(post.getPostId());
        pvm.setPost(post.getPost());
        pvm.setPostDate(post.getPostDate());
        pvm.setPosterName(post.getPosterName());

        List<String> commentsList = post.getComments();
        if (commentsList != null) {
            pvm.setComments(commentsList);
        }

        return buildPostViewModel(pvm.getPostId());
    }


    public PostViewModel getPost(Integer postId) throws NoSuchPostException {
        Post post;
        try {
            post = postClient.getPost(postId);
        } catch (FeignException.NotFound fe) {
            System.out.println("Post " + postId + " could not be found. " + fe.getMessage());
            throw new NoSuchPostException(postId);
        }

        return buildPostViewModel(postId);
    }

    public List<PostViewModel> getAllPostsByName(String posterName) {
        List<Post> postList = postClient.getAllPostsByName(posterName);
        List<PostViewModel> pvm = new ArrayList<>();

        for (Post p : postList) {
            pvm.add(buildPostViewModel(p.getPostId()));
        }
        return pvm;
    }

    public List<PostViewModel> getAllPosts() {
        List<Post> postList = postClient.getAllPosts();
        List<PostViewModel> pvm = new ArrayList<>();

        for (Post p : postList) {
            pvm.add(buildPostViewModel(p.getPostId()));
        }
        return pvm;
    }

    public void updatePost(PostViewModel pvm) throws NoSuchPostException {
        try {
            int id = postClient.getPost(pvm.getPostId()).getPostId();
        } catch (FeignException.NotFound fe) {
            System.out.println("Post " + pvm.getPostId() + " could not be found. ");
            throw new NoSuchPostException(pvm.getPostId());
        }

        Post post = new Post();
        post.setPostId(pvm.getPostId());
        post.setPost(pvm.getPost());
        post.setPostDate(pvm.getPostDate());
        post.setPosterName(pvm.getPosterName());
        post.setComments(pvm.getComments());

        postClient.updatePost(post, pvm.getPostId());
    }

    public void deletePost(Integer postId) {
        postClient.deletePost(postId);
    }

    public CommentViewModel createComment(CommentViewModel cvm) {
        Comment comment = new Comment();
        comment.setPostId(cvm.getPostId());
        comment.setCreateDate(cvm.getCreateDate());
        comment.setCommenterName(cvm.getCommenterName());
        comment.setComment(cvm.getComment());

        comment = commentClient.createComment(comment);

        cvm.setCommentId(comment.getCommentId());
        cvm.setPostId(comment.getPostId());
        cvm.setCreateDate(comment.getCreateDate());
        cvm.setCommenterName(comment.getCommenterName());
        cvm.setComment(comment.getComment());

        return cvm;
    }

    public CommentViewModel getComment(Integer commentId) throws NoSuchCommentException {
        Comment comment;
        try {
            comment = commentClient.getComment(commentId);
        } catch (FeignException.NotFound fe) {
            System.out.println("Comment " + commentId + "could not be found. " + fe.getMessage());
            throw new NoSuchCommentException(commentId);
        }

        return buildCommentViewModel(commentId);
    }

    public List<CommentViewModel> getAllComments() {
        List<Comment> commentsList = commentClient.getAllComments();
        List<CommentViewModel> cvm = new ArrayList<>();

        for (Comment c : commentsList) {
            cvm.add(buildCommentViewModel(c.getCommentId()));
        }
        return cvm;
    }

    public List<CommentViewModel> getAllCommentsByName(String commenterName) {
        List<Comment> commentsList = commentClient.getAllCommentsByName(commenterName);
        List<CommentViewModel> cvm = new ArrayList<>();

        for (Comment c : commentsList) {
            cvm.add(buildCommentViewModel(c.getCommentId()));
        }
        return cvm;
    }

    public List<CommentViewModel> getAllCommentsByPostId(Integer postId) {
        List<Comment> commentsList = commentClient.getAllCommentsByPostId(postId);
        List<CommentViewModel> cvm = new ArrayList<>();

        for (Comment c : commentsList) {
            cvm.add(buildCommentViewModel(c.getCommentId()));
        }
        return cvm;
    }

    public void updateComment(CommentViewModel cvm) throws NoSuchCommentException {
        try {
            int exists = commentClient.getComment(cvm.getCommentId()).getCommentId();
        } catch (FeignException.NotFound fe) {
            System.out.println("Comment " + cvm.getCommentId() + " could not be found.");
            throw new NoSuchCommentException(cvm.getCommentId());
        }

        Comment comment = new Comment();
        comment.setCommentId(cvm.getCommentId());
        comment.setPostId(cvm.getPostId());
        comment.setCreateDate(cvm.getCreateDate());
        comment.setCommenterName(cvm.getCommenterName());
        comment.setComment(cvm.getComment());

        commentClient.updateComment(comment, cvm.getCommentId());
    }

    public void deleteComment(int commentId) {
        commentClient.deleteComment(commentId);
    }

    private PostViewModel buildPostViewModel(Integer postId) {
        List<Comment> commentsList = commentClient.getAllCommentsByPostId(postId);
        List<String> stringComments = new ArrayList<>();

//        if (commentsList == null) {
//            return null;
//        } else {
//            for (Comment c : commentsList) {
//                stringComments.add(c.getComment());
//            }
//        }

        for (Comment c : commentsList) {
            stringComments.add(c.getComment());
        }

        PostViewModel pvm = new PostViewModel();
        pvm.setPostId(postId);
        pvm.setPost(postClient.getPost(postId).getPost());
        pvm.setPostDate(postClient.getPost(postId).getPostDate());
        pvm.setPosterName(postClient.getPost(postId).getPosterName());
        pvm.setComments(stringComments);

        return pvm;
    }

    private CommentViewModel buildCommentViewModel(Integer commentId) {
        CommentViewModel cvm = new CommentViewModel();
        cvm.setCommentId(commentId);
        cvm.setPostId(commentClient.getComment(commentId).getPostId());
        cvm.setCreateDate(commentClient.getComment(commentId).getCreateDate());
        cvm.setCommenterName(commentClient.getComment(commentId).getCommenterName());
        cvm.setComment(commentClient.getComment(commentId).getComment());

        return cvm;
    }
}
