package com.trilogyed.stwitter.exception;


public class NoSuchPostException extends RuntimeException {
    public NoSuchPostException(int postId) {
        super("There is no such post with post id " + postId + ".");
    }
}
