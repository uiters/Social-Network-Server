package uit.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import uit.core.entity.User;

@FeignClient(name="auth-server")
public interface AuthServerFeign {
    @GetMapping("/user/email/{email}")
    User getByUserName(@PathVariable String email);

    @GetMapping("/user/{id}")
    User getById(@PathVariable Long id);
}
