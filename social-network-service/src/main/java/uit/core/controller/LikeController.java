package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uit.core.entity.Like;
import uit.core.service.LikeService;

import java.util.List;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @GetMapping("/all")
    public List<Like> getAll() {
        return likeService.getAll();
    }

    @GetMapping
    public List<String> getAllOfPost(@RequestParam long postId) {
        return likeService.getAllOfPost(postId);
    }


//    @GetMapping("/{id}")
//    public Like getById(@PathVariable Long id) {
//        return likeService.getById(id);
//    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PostMapping
    public Like create(@RequestBody Like like) {
        return likeService.create(like);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/{id}")
    public Like update(@RequestBody Like like, @PathVariable Long id) {
        return likeService.update(like, id);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @DeleteMapping()
    public void deleteById(@RequestBody Like like) {
        likeService.deleteById(like);
    }
}
