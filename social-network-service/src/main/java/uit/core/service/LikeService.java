package uit.core.service;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.core.entity.Like;
import uit.core.entity.User;
import uit.core.feign.AuthServerFeign;
import uit.core.repository.LikeRepository;
import uit.core.util.SocialUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private AuthServerFeign authServerFeign;

    public List<Like> getAll() {
        return likeRepository.findAll();
    }


    public List<String> getAllOfPost(long postId) {
        List<String> result = new ArrayList();
        List<Like> likes = likeRepository.findAllByPostId(postId);
        likes.stream().forEach((like) -> {
            User user = authServerFeign.getById(like.getUserId());
            result.add(user.getUsername());
        });
        return result;
    }

    public Like getById(Long id) {
        return likeRepository.findById(id).get();
    }

    public Like create(Like like) {

        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        Optional<Like> optionalLike = likeRepository.findByUserIdAndPostId(user.getId(), like.getPostId());
        if (optionalLike.isPresent()) return optionalLike.get();

        like.setUserId(user.getId());
        return likeRepository.save(like);
    }

    public Like update(Like like, Long id) {
        like.setId(id);
        return likeRepository.save(like);
    }

    public void deleteById(Like like) {
        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        likeRepository.deleteByPostIdAndUserId(like.getPostId(), user.getId());
    }

}
