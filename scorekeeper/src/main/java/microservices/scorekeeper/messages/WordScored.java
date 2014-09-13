package microservices.scorekeeper.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WordScored {
    public String id;
    public long score;
}
