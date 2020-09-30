package uit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.core.entity.Like;
import uit.core.repository.LikeRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    public List<String> getAll(long postId) {
        List<String> result = new ArrayList();
        List<Like> likes = likeRepository.findAllByPostId(postId);
        likes.stream().forEach((like) -> {
            result.add(like.getUserId().toString());
        });
        return result;
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

    public void deleteById(Like like) {
        likeRepository.deleteByPostIdAndUserId(like.getPostId(), like.getUserId());
    }


}
