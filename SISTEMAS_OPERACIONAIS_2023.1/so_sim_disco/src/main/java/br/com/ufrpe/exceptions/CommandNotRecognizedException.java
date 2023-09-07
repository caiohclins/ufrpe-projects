package br.com.ufrpe.exceptions;

public class CommandNotRecognizedException extends RuntimeException {

    public CommandNotRecognizedException(String message) {
        super(message);
    }

    
}
