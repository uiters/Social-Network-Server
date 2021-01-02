package uit.core.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import uit.core.entity.Notification;
import uit.core.entity.Post;
import uit.core.entity.User;
import uit.core.entity.event.Action;
import uit.core.entity.event.Level;
import uit.core.entity.event.UserAction;
import uit.core.entity.event.UserLevel;
import uit.core.feign.AuthServerFeign;
import uit.core.repository.NotificationRepository;
import uit.core.repository.PostRepository;
import uit.core.repository.event.ActionRepository;
import uit.core.repository.event.LevelRepository;
import uit.core.repository.event.UserLevelRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class CareListener implements ApplicationListener<CareEvent> {
    private static final Logger LOGGER = Logger.getLogger(CareListener.class.getName());

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private AuthServerFeign authServerFeign;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void onApplicationEvent(CareEvent careEvent) {
        LOGGER.info("Event is happening: " + careEvent.toString());
        UserLevel userLevel = new UserLevel();
        UserAction userAction = careEvent.getUserAction();

        Optional<UserLevel> userLevelOptional = userLevelRepository.findByUserIdAndPostId(userAction.getUserId(), userAction.getPostId());
        if (userLevelOptional.isPresent()) {
            userLevel = userLevelOptional.get();
        } else {
            userLevel.setUserId(userAction.getUserId());
            userLevel.setPostId(userAction.getPostId());
        }

        long actionPoint = 0;
        try {
            actionPoint = getActionPoint(userAction);
        } catch (Exception e) {
            return;
        }

        userLevel.setPoint(userLevel.getPoint() + actionPoint);

        long currentLevel = userLevel.getLevelId();

        Level level = caculateLevel(userLevel);

        userLevel.setLevelId(level.getId());

        userLevelRepository.save(userLevel);

        if (level.getId() != currentLevel) {
            LOGGER.info("Level changed to " + level.getName());
            try {
                recommend(userAction, userLevel, level);
            } catch (Exception e) {
                LOGGER.info("Not define recommend action for level " + level.getName());
            }
        }

    }

    private void recommend(UserAction userAction, UserLevel userLevel, Level level) throws Exception {
        //RecommendationType recommendationType = RecommendationType.getRecommendationType(userLevel.getLevelId());
        LOGGER.info("Recommendation type is " + level.getName());

        switch (level.getName()) {
            case "START_INTERESTED":
                pushNotification(userLevel, userAction);
                break;
            case "INTERESTED":
                suggestChat(userLevel, userAction);
                break;
            case "VERY_INTERESTED":
                suggestMeeting(userLevel, userAction);
                break;
            default:
                throw new Exception("Not define recommend action for level " + level.getName() );
        }
    }

    private void suggestMeeting(UserLevel userLevel, UserAction userAction) {
        User user = authServerFeign.getById(userLevel.getUserId());
        User author = getUserByPostId(userLevel.getPostId());

        Notification notification = new Notification();
        notification.setAvatar(user.getAvatar());
        notification.setMessage(user.getUsername() + " đang quan tâm về bài viết của bạn.Tạo Video Meeting và mời "+ user.getUsername() + " để trao đổi trực tiếp ?");
        notification.setURL("/post/" + userLevel.getPostId());
        notification.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        notification.setType(RecommendationType.MEETING.getRecommendationAction());
        notification.setSecondUserId(user.getId());

        if (author.getId() == user.getId()) {
            LOGGER.info("user is author");
            return;
        }

        simpMessagingTemplate.convertAndSendToUser(author.getUsername(), "/notification/social", notification);

        notification.setUserId(author.getId());
        notificationRepository.save(notification);
    }

    private void suggestChat(UserLevel userLevel, UserAction userAction) {
        User user = authServerFeign.getById(userLevel.getUserId());
        User author = getUserByPostId(userLevel.getPostId());

        Notification notification = new Notification();
        notification.setAvatar(user.getAvatar());
        notification.setMessage(user.getUsername() + " đang quan tâm về bài viết của bạn.Nhắn tin để trao đổi trực tiếp với " + user.getUsername() + " ?");
        notification.setURL("/post/" + userLevel.getPostId());
        notification.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        notification.setType(RecommendationType.CHAT.getRecommendationAction());
        notification.setSecondUserId(user.getId());

        if (author.getId() == user.getId()) {
            LOGGER.info("user is author");
            return;
        }

        simpMessagingTemplate.convertAndSendToUser(author.getUsername(), "/notification/social", notification);

        notification.setUserId(author.getId());
        notificationRepository.save(notification);
    }

    private void pushNotification(UserLevel userLevel, UserAction userAction) {
        User user = authServerFeign.getById(userLevel.getUserId());
        User author = getUserByPostId(userLevel.getPostId());

        Notification notification = new Notification();
        notification.setAvatar(user.getAvatar());
        notification.setMessage(user.getUsername() + " đang quan tâm về bài viết của bạn");
        notification.setURL("/post/" + userLevel.getPostId());
        notification.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        notification.setType(RecommendationType.PUSH_NOTI.getRecommendationAction());
        notification.setSecondUserId(user.getId());

        if (author.getId() == user.getId()) {
            LOGGER.info("user is author of a post. Not push notification.");
            return;
        }

        simpMessagingTemplate.convertAndSendToUser(author.getUsername(), "/notification/social", notification);

        notification.setUserId(author.getId());
        notificationRepository.save(notification);
    }

    private User getUserByPostId(long postId) {
        Post post = postRepository.findById(postId).get();
        User user = authServerFeign.getById(post.getUserId());
        return user;
    }

    private Level caculateLevel(UserLevel userLevel) {
        List<Level> levels = levelRepository.findAll();
        levels.sort(Comparator.comparing(Level::getActivePoint).reversed());

        for (Level level : levels) {
            if (userLevel.getPoint() >= level.getActivePoint())  {
             return level;
            }
        }

        return levels.get(levels.size()-1);
    }

    private long getActionPoint(UserAction userAction) throws Exception {
        Optional<Action> optionalAction = actionRepository.findByAction(userAction.getActionId());
        if (optionalAction.isPresent()) {
            Action action = optionalAction.get();
            return action.getPoint();
        }
        else throw new RuntimeException("Action not define");

    }
}
