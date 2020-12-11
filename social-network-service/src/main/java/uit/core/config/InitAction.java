package uit.core.config;

import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import uit.core.entity.event.Action;
import uit.core.entity.event.Level;
import uit.core.repository.event.ActionRepository;
import uit.core.repository.event.LevelRepository;

@Configuration
@ConditionalOnProperty(prefix = "social", name = "initAction", matchIfMissing = false)
public class InitAction implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitAction.class);

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("init default action");
        actionRepository.deleteAll();
//        defineActionLike();
//        defineActionComment();
        defineAction();
        LOGGER.info("define action successfully");

        LOGGER.info("init level");
        defineNoCareLevel();
        defineStartInterestedLevel();
        defineInterestedLevel();
        defineVeryInterestedLevel();
        LOGGER.info("init level successfully");
    }

    private void defineAction() {
        for (uit.core.event.Action actionItem : uit.core.event.Action.values()) {
            Action action = new Action();
            action.setAction(actionItem.getCode());
            action.setPoint(actionItem.getPoint());
            actionRepository.save(action);
        }
    }

    private void defineNoCareLevel() {
        Level level = new Level();
        level.setLevel(uit.core.event.Level.NO_CARE.getCode());
        level.setActivePoint(0);
        levelRepository.save(level);
    }

    private void defineStartInterestedLevel() {
        Level level = new Level();
        level.setLevel(uit.core.event.Level.START_INTERESTED.getCode());
        level.setActivePoint(5);
        levelRepository.save(level);
    }

    private void defineInterestedLevel() {
        Level level = new Level();
        level.setLevel(uit.core.event.Level.INTERESTED.getCode());
        level.setActivePoint(8);
        levelRepository.save(level);
    }

    private void defineVeryInterestedLevel() {
        Level level = new Level();
        level.setLevel(uit.core.event.Level.VERY_INTERESTED.getCode());
        level.setActivePoint(11);
        levelRepository.save(level);
    }




}
