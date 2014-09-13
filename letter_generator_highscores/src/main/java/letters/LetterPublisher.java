package letters;

import java.io.IOException;

final class LetterPublisher {

    private final Publisher publisher;

    public LetterPublisher(final Publisher publisher) throws IOException {

        this.publisher = publisher;
    }

    public void publishLetterForGame(String id, final Character letter) {
        final String message = "{\"id\": \"" + id + "\", \"letter\": \"" + letter + "\"}";
        publisher.publish(message);
    }
}
