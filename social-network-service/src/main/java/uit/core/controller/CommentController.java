package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uit.core.dto.request.CommentRequest;
import uit.core.dto.response.CommentResponse;
import uit.core.entity.Comment;
import uit.core.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{postId}")
    public CommentResponse getAll(@PathVariable long postId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int limit) {
        return commentService.getAll(postId, page, limit);
    }

    @PostMapping
    public Comment create(@RequestBody CommentRequest commentRequest) {
        return commentService.create(commentRequest);
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
