package handlers;

import request_responses.ClearAllRequest;

public class ClearAllHandler implements Handler {

    public ClearAllRequest deserialize(String json) {
        return serializer.deserializeClearAll(json);
    }
}
