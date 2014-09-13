package microservices.scorekeeper.messages;


public class GameScore {
    public String id;
    public long score;

    public GameScore(String id, long score) {
        this.id = id;
        this.score = score;
    }
}
