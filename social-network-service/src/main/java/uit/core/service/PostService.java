package uit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.core.entity.Post;
import uit.core.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post getById(Long id) {
        return postRepository.findById(id).get();
    }

    public Post create(Post post) {
        return postRepository.save(post);
    }

    public Post update(Post post, Long id) {
        post.setId(id);
        return postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
