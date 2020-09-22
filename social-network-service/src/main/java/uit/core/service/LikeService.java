package uit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.core.entity.Like;
import uit.core.repository.LikeRepository;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    public List<Like> getAll() {
        return likeRepository.findAll();
    }

    public Like getById(Long id) {
        return likeRepository.findById(id).get();
    }

    public Like create(Like like) {
        return likeRepository.save(like);
    }

    public Like update(Like like, Long id) {
        like.setId(id);
        return likeRepository.save(like);
    }

    public void deleteById(Long id) {
        likeRepository.deleteById(id);
    }
}
