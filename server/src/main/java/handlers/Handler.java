package handlers;

import request_responses.RegisterRequest;

public interface Handler<T> {

    public Serializer serializer = new Serializer();

    public default String serialize(T response) {
        return serializer.serialize(response);
    }
}
