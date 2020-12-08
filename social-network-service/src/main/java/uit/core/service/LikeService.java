package uit.core.service;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uit.core.dto.response.CommentItem;
import uit.core.entity.Like;
import uit.core.entity.Notification;
import uit.core.entity.Post;
import uit.core.entity.User;
import uit.core.entity.event.UserAction;
import uit.core.event.Action;
import uit.core.event.CareEvent;
import uit.core.feign.AuthServerFeign;
import uit.core.repository.LikeRepository;
import uit.core.repository.NotificationRepository;
import uit.core.repository.PostRepository;
import uit.core.repository.event.UserActionRepository;
import uit.core.util.SocialUtil;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Component
public class LikeService {
    @Autowired
    private ApplicationEventPublisher publisher;

//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//        this.publisher = applicationEventPublisher;
//    }

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private AuthServerFeign authServerFeign;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserActionRepository userActionRepository;

    private final long COMMENT = 2;

    public List<Like> getAll() {
        return likeRepository.findAll();
    }

    public List<String> getAllOfPost(long postId) {
        List<String> result = new ArrayList();
        List<Like> likes = likeRepository.findAllByPostId(postId);
        likes.stream().forEach((like) -> {
            User user = authServerFeign.getById(like.getUserId());
            result.add(user.getUsername());
        });
        return result;
    }

    public Like getById(Long id) {
        return likeRepository.findById(id).get();
    }

    public Like create(Like like) {

        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        Optional<Like> optionalLike = likeRepository.findByUserIdAndPostId(user.getId(), like.getPostId());
        if (optionalLike.isPresent()) return optionalLike.get();

        like.setUserId(user.getId());

        pushNotification(user, like.getPostId());

        publishLikeEvent(like);

        return likeRepository.save(like);
    }

    private void publishLikeEvent(Like like) {
        UserAction userAction = new UserAction();
        userAction.setUserId(like.getUserId());
        userAction.setActionId(Action.LIKE.getCode());
        userAction.setPostId(like.getPostId());

        userActionRepository.save(userAction);

        publisher.publishEvent(new CareEvent(this, userAction));

    }

    private void pushNotification(User user, long postId) {
        Notification notification = new Notification();
        notification.setAvatar(user.getAvatar());
        notification.setMessage(user.getUsername() + " đã thích bài viết của bạn");
        notification.setURL("/post/" + postId);
        notification.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        notification.setType(COMMENT);

        User author = getUserByPostId(postId);

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

    public Like update(Like like, Long id) {
        like.setId(id);
        return likeRepository.save(like);
    }

    public void deleteById(Like like) {
        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        likeRepository.deleteByPostIdAndUserId(like.getPostId(), user.getId());
    }


}
