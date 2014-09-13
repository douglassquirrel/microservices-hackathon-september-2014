package microservices.scorekeeper.messages;


public class TotalScore {
    public String id;
    public long score;

    public TotalScore(String id, long score) {
        this.id = id;
        this.score = score;
    }
}
