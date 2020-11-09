package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uit.core.dto.response.NotificationResponse;
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
}
