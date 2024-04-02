namespace OrderAggregate.Exceptions;

public class AlreadyShippedException() : InvalidOperationException("Already shipped");
