package uit.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uit.core.entity.User;

@FeignClient(name="auth-server")
public interface AuthServerFeign {
    @GetMapping("/user/username/{username}")
    User getByUserName(@PathVariable String username);

    @GetMapping("/user/{id}")
    User getById(@PathVariable Long id);

    @PostMapping("/user/disable/{userId}")
    User disableUser(@PathVariable long userId);

    @PostMapping("/user/disable/{userId}")
    User enableUser(@PathVariable long userId);

    @PostMapping("/user/admin/create")
    User createAdmin(@RequestBody User user);
}
