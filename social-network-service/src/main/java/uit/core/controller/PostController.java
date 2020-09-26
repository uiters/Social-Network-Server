package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;
import uit.core.dto.response.PostItem;
import uit.core.dto.response.PostResponse;
import uit.core.entity.Post;
import uit.core.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public PostResponse getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int limit) {
        return postService.getAll(page, limit);
    }

//    @GetMapping("/test")
//    public Page<Post> getAllTest(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int limit) {
//        return postService.getAllTest(page, limit);
//    }

    @GetMapping("/{id}")
    public PostItem getById(@PathVariable Long id) {
        return postService.getById(id);
    }

    @PostMapping
    public PostItem create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping("/{id}")
    public Post update(@RequestBody Post post, @PathVariable Long id) {
        return postService.update(post, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        postService.deleteById(id);
    }
}
