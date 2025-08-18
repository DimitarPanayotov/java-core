package bg.sofia.uni.fmi.mjt.newsfeed.api;

import bg.sofia.uni.fmi.mjt.newsfeed.exception.ApiException;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.BadRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.ServerErrorException;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ResponseHandler {
    private ResponseHandler() {
    }

    public static NewsResponse handle(HttpResponse response, Gson gson) throws ApiException {
        int code = response.getStatusCode();
        String body = response.getBody();

        if (code == 200) {
            return gson.fromJson(body, NewsResponse.class);
        }

        String message = ResponseHandler.extractMessage(body);
        switch (code) {
            case 400 -> throw new BadRequestException(message);
            case 401 -> throw new UnauthorizedException(message);
            case 429 -> throw new TooManyRequestsException(message);
            default -> {
                if (code >= 500) throw new ServerErrorException(message);
                throw new ApiException("HTTP" + code + ": " + message);
            }
        }
    }

    private static String extractMessage(String body) {
        try {
            JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
            if (obj.has("message")) {
                return obj.get("message").getAsString();
            }
            if (obj.has("error")) {
                return obj.get("error").getAsString();
            }
        } catch (Exception ignored) {
        }
        return body == null ? "Unknown error" : body;
    }
}

