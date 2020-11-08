package uit.core.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uit.core.dto.request.CommentRequest;
import uit.core.dto.response.CommentItem;
import uit.core.dto.response.CommentResponse;
import uit.core.dto.response.PostItem;
import uit.core.dto.response.PostResponse;
import uit.core.entity.Comment;
import uit.core.entity.Notification;
import uit.core.entity.Post;
import uit.core.entity.User;
import uit.core.feign.AuthServerFeign;
import uit.core.repository.CommentRepository;
import uit.core.repository.PostRepository;
import uit.core.util.SocialUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private static final ModelMapper modelMapper = new ModelMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthServerFeign authServerFeign;

    @Autowired
    private PostRepository postRepository;

    public CommentResponse getAll(long postId, int page, int limit) {
        CommentResponse commentResponse = new CommentResponse();
        Pageable paging = PageRequest.of(page, limit);
        Page<Comment> response = commentRepository.findAllByPostId(postId, paging);

        List<CommentItem> commentItems = new ArrayList();
        for (Comment comment : response.getContent()) {
            CommentItem commentItem = modelMapper.map(comment, CommentItem.class);

            User user = authServerFeign.getById(comment.getUserId());
            commentItem.setUserId(user.getId());
            commentItem.setUsername(user.getUsername());
            commentItem.setAvatar(user.getAvatar());

            commentItems.add(commentItem);
        }

        commentResponse.setItems(commentItems);
        if (page < response.getTotalPages()-1) {
            commentResponse.setHasNext(true);
        } else {
            commentResponse.setHasNext(false);
        }
        String nextLink = "/comment?&page=".concat(String.valueOf(page+1));
        commentResponse.setNextLink(nextLink);

        commentResponse.setPostId(postId);
        return commentResponse;
    }

    public Comment getById(Long id) {
        return commentRepository.findById(id).get();
    }

    public CommentItem create(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setPostId(commentRequest.getPostId());

        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        comment.setUserId(user.getId());
        Comment savedComment = commentRepository.save(comment);

        CommentItem commentItem = modelMapper.map(savedComment, CommentItem.class);
        commentItem.setUserId(user.getId());
        commentItem.setUsername(user.getUsername());
        commentItem.setAvatar(user.getAvatar());

        pushNotification(commentItem, commentRequest.getPostId());
        return commentItem;
    }

    private void pushNotification(CommentItem commentItem, long postId) {
        Notification notification = new Notification();
        notification.setAvatar(commentItem.getAvatar());
        notification.setMessage(commentItem.getUsername() + " đã bình luận về bài viết của bạn");
        notification.setURL("/post/" + postId);

        String username = getUserByPostId(postId);
        //simpMessagingTemplate.convertAndSend("/topic/notification", notification);
        //Client will subcribe at /user/queue/notification
        simpMessagingTemplate.convertAndSendToUser(username, "/notification/social", notification);
        LOGGER.info("push notifiaction to client");
        LOGGER.trace("push notifiaction to client");
        LOGGER.debug("push notifiaction to client");
    }

    private String getUserByPostId(long postId) {
        Post post = postRepository.findById(postId).get();
        User user = authServerFeign.getById(post.getUserId());
        return user.getUsername();
    }

    public CommentItem update(Comment comment, Long id) {
        Comment dbComment = commentRepository.findById(id).get();
        dbComment.setContent(comment.getContent());

        Comment savedComment = commentRepository.save(dbComment);

        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        CommentItem commentItem = modelMapper.map(savedComment, CommentItem.class);
        commentItem.setUserId(user.getId());
        commentItem.setUsername(user.getUsername());
        commentItem.setAvatar(user.getAvatar());
        return commentItem;
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
