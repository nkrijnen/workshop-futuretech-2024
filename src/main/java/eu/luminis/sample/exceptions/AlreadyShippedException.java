package eu.luminis.sample.exceptions;

public class AlreadyShippedException extends IllegalStateException {
    public AlreadyShippedException() {
        super("Already shipped");
    }
}
