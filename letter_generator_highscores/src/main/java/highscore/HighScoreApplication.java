package highscore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class HighScoreApplication {

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(HighScoreApplication.class, args);
    }
}
