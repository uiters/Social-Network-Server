package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uit.core.entity.Like;
import uit.core.service.LikeService;

import java.util.List;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @GetMapping
    public List<Like> getAll() {
        return likeService.getAll();
    }

    @GetMapping("/{id}")
    public Like getById(@PathVariable Long id) {
        return likeService.getById(id);
    }

    @PostMapping
    public Like create(@RequestBody Like like) {
        return likeService.create(like);
    }

    @PutMapping("/{id}")
    public Like update(@RequestBody Like like, @PathVariable Long id) {
        return likeService.update(like, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        likeService.deleteById(id);
    }
}
