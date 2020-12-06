package uit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uit.core.dto.response.CommentResponse;
import uit.core.dto.response.NotificationResponse;
import uit.core.entity.Comment;
import uit.core.entity.Notification;
import uit.core.entity.User;
import uit.core.feign.AuthServerFeign;
import uit.core.repository.NotificationRepository;
import uit.core.util.SocialUtil;

import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AuthServerFeign authServerFeign;

    public NotificationResponse getByUserId(int page, int limit) {
        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());

        NotificationResponse notiResponse = new NotificationResponse();
        Pageable paging = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> response = notificationRepository.findAllByUserId(user.getId(), paging);

        notiResponse.setItems(response.getContent());
        if (page < response.getTotalPages()-1) {
            notiResponse.setHasNext(true);
        } else {
            notiResponse.setHasNext(false);
        }
        String nextLink = "/notification?page=".concat(String.valueOf(page+1));
        notiResponse.setNextLink(nextLink);
        return notiResponse;
    }

    public Notification markAsReaded(long id) throws Exception {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (!notificationOpt.isPresent()) {
            throw new Exception("notification id not found");
        }
        Notification notification = notificationOpt.get();
        notification.setRead(true);
        return notification;
    }
}
