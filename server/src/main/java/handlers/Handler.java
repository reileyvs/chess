package handlers;

public interface Handler<T> {

    Serializer SERIALIZER= new Serializer();

    default String serialize(T response) {
        return SERIALIZER.serialize(response);
    }
}
