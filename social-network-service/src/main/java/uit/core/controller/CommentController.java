package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uit.core.entity.Comment;
import uit.core.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @GetMapping("/{id}")
    public Comment getById(@PathVariable Long id) {
        return commentService.getById(id);
    }

    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        return commentService.create(comment);
    }

    @PutMapping("/{id}")
    public Comment update(@RequestBody Comment comment, @PathVariable Long id) {
        return commentService.update(comment, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
    }
}
