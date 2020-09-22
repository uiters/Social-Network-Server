package uit.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uit.auth.entity.Friend;
import uit.auth.service.FriendService;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @GetMapping
    public List<Friend> getAll() {
        return friendService.getAll();
    }

    @GetMapping("/{id}")
    public Friend getById(@PathVariable Long id) {
        return friendService.getById(id);
    }

    @PostMapping
    public Friend create(@RequestBody Friend friend) {
        return friendService.create(friend);
    }

    @PutMapping("/{id}")
    public Friend update(@RequestBody Friend friend, @PathVariable Long id) {
        return friendService.update(friend, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        friendService.deleteById(id);
    }
}
