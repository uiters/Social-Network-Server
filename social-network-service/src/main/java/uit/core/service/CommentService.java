package uit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.core.entity.Comment;
import uit.core.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Comment getById(Long id) {
        return commentRepository.findById(id).get();
    }

    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment update(Comment comment, Long id) {
        comment.setId(id);
        return commentRepository.save(comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
