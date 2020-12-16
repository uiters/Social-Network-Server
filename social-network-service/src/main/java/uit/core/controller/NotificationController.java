package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uit.core.dto.response.NotificationResponse;
import uit.core.entity.Notification;
import uit.core.entity.event.UserAction;
import uit.core.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("#oauth2.hasScope('ui')")
    @GetMapping
    public NotificationResponse getByUserId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int limit) {
        return notificationService.getByUserId(page, limit);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PostMapping("/read/{id}")
    public Notification markAsReaded(@PathVariable long id) throws Exception {
        return notificationService.markAsReaded(id);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PostMapping("/read/count/{postId}")
    public UserAction readPost15s(@PathVariable long postId) throws Exception {
        return notificationService.readPost15s(postId);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PostMapping("/read/detail/{postId}")
    public UserAction readDetailPost(@PathVariable long postId) throws Exception {
        return notificationService.readDetailPost(postId);
    }
}
