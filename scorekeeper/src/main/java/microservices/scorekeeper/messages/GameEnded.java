package microservices.scorekeeper.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameEnded {
    public String id;
}
