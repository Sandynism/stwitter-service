package com.trilogyed.stwitter.exception;


public class NoSuchCommentException extends RuntimeException {
    public NoSuchCommentException(int commentId) {
        super("There is no such comment with comment id " + commentId + ".");
    }
}
