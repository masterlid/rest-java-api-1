package tv.lid.cinema.api.transformers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.ResponseTransformer;

// класс для преобразования ответа в строку JSON
public final class JsonTransformer implements ResponseTransformer {
    // статический экземпляр трансформера
    public static final JsonTransformer JSON = new JsonTransformer();

    @Override
    public String render(Object response) {
        try {
            return (new ObjectMapper()).writeValueAsString(response);
        } catch (JsonProcessingException exc) {
            return null;
        }
    }
}
