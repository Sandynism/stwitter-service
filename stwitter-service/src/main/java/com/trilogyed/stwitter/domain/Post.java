package com.trilogyed.stwitter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Post {
    private Integer postId;
    @Size(min = 1, max = 255, message = "Post must not be empty")
    private String post;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate postDate;
    @Size(min = 1, max = 50, message = "Name must not be empty")
    private String posterName;
    private List<String> comments;

    public Post() {
    }

    public Post(Integer postId, String post, LocalDate postDate, String posterName, List<String> comments) {
        this(post, postDate, posterName, comments);
        this.postId = postId;
    }

    public Post(String post, LocalDate postDate, String posterName, List<String> comments) {
        this.post = post;
        this.postDate = postDate;
        this.posterName = posterName;
        this.comments = comments;
    }


    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post1 = (Post) o;
        return Objects.equals(getPostId(), post1.getPostId()) &&
                Objects.equals(getPost(), post1.getPost()) &&
                Objects.equals(getPostDate(), post1.getPostDate()) &&
                Objects.equals(getPosterName(), post1.getPosterName()) &&
                Objects.equals(getComments(), post1.getComments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostId(), getPost(), getPostDate(), getPosterName(), getComments());
    }
}

