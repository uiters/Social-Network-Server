package uit.core.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import uit.core.dto.request.EventRequest;
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
import uit.core.util.SocialUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class CareListener implements ApplicationListener<CareEvent> {
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

        userLevel.setLevelId(level.getLevel());

        userLevelRepository.save(userLevel);

        if (level.getLevel() != currentLevel) {
            recommend(userAction, userLevel);
        }

    }

    private void recommend(UserAction userAction, UserLevel userLevel) {
        RecommendationType recommendationType = RecommendationType.getRecommendationType(userLevel.getLevelId());
        switch ((int) recommendationType.getRecommendationAction()) {
            case 1:
                pushNotification(userLevel, userAction);
                break;
            case 2:
                suggestChat(userLevel, userAction);
                break;
            case 3:
                suggestMeeting(userLevel, userAction);
                break;
            default:
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
        notification.setType(uit.core.event.Action.MEETING.getCode());

        if (author.getId() == user.getId()) return;

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
        notification.setType(uit.core.event.Action.CHAT.getCode());

        if (author.getId() == user.getId()) return;

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
        notification.setType(3);

        if (author.getId() == user.getId()) return;

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
        else throw new Exception("Action not define");

    }
}
