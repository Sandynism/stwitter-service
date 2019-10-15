package com.trilogyed.stwitter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CommentViewModel implements Serializable {

    private Integer commentId;
    private Integer postId;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    @Size(min = 1, max = 50, message = "Name must not be empty")
    private String commenterName;
    @Size(min = 1, max = 255, message = "Comment must not be empty")
    private String comment;


    public CommentViewModel() {
    }

    public CommentViewModel(Integer commentId, Integer postId, LocalDate createDate, String commenterName, String comment) {
        this(postId, createDate, commenterName, comment);
        this.commentId = commentId;
    }

    public CommentViewModel(Integer postId, LocalDate createDate, String commenterName, String comment) {
        this.postId = postId;
        this.createDate = createDate;
        this.commenterName = commenterName;
        this.comment = comment;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentViewModel that = (CommentViewModel) o;
        return getCommentId().equals(that.getCommentId()) &&
                getPostId().equals(that.getPostId()) &&
                getCreateDate().equals(that.getCreateDate()) &&
                getCommenterName().equals(that.getCommenterName()) &&
                getComment().equals(that.getComment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentId(), getPostId(), getCreateDate(), getCommenterName(), getComment());
    }

    @Override
    public String toString() {
        return "Comment{" + "commentId " + commentId + ", " + "postId " + postId + ", " +
                "createDate: " + createDate + ", " + "commenterName: " + commenterName +
                ", " + "comment= " + comment + '}';
    }

}
