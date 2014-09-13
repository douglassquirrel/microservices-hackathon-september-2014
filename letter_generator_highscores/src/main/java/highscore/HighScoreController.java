package highscore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import letters.Consumer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.sort;

@RestController
final class HighScoreController {

    private static final Map<String, TotalScore> totalScores = new HashMap<>();

    @RequestMapping("/scores")
    public Collection<TotalScore> scores() {
        final List<TotalScore> scores = new ArrayList<>(totalScores.values());
        sort(scores, new Comparator<TotalScore>() {
            @Override
            public int compare(final TotalScore o1, final TotalScore o2) {
                return o1.getScore() > o2.getScore() ? -1 : 1;
            }
        });
        return scores;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("54.76.117.95");
        final Consumer scoreConsumer = new Consumer(factory, "game.score");
        final Consumer gameEndedConsumer = new Consumer(factory, "game.ended");
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final String body = scoreConsumer.consumeNext();

                    try {
                        final TotalScore score = new ObjectMapper().readValue(body, TotalScore.class);
                        if (totalScores.containsKey(score.getId())) {
                            totalScores.get(score.getId()).setScore(score.getScore());
                        } else {
                            totalScores.put(score.getId(), score);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final String body = gameEndedConsumer.consumeNext();

                    try {
                        final GameEnded ended = new ObjectMapper().readValue(body, GameEnded.class);
                        if (totalScores.containsKey(ended.getId())) {
                            totalScores.get(ended.getId()).setFinished(true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public static class TotalScore {
        public String id;
        public int score;
        public boolean finished = false;

        public void setId(final String id) {
            this.id = id;
        }

        public void setScore(final int score) {
            this.score = score;
        }

        public String getId() {
            return id;
        }

        public int getScore() {
            return score;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(final boolean finished) {
            this.finished = finished;
        }
    }

    public static class GameEnded {

        private String id;

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }
    }
}
