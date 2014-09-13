package letters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

final class WordsConsumer {

    private final ObjectMapper objectMapper;
    private final Consumer consumer;

    public WordsConsumer(final Consumer consumer,
                         final ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        this.consumer = consumer;
    }

    @SuppressWarnings("unchecked")
    Map<String,Object> receiveNextWords() throws IOException {
        return objectMapper
                .readValue(consumer.consumeNext(), Map.class);

    }
}
