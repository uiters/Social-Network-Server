package uit.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.core.dto.response.PostItem;
import uit.core.dto.response.PostResponse;
import uit.core.entity.Image;
import uit.core.entity.Post;
import uit.core.entity.User;
import uit.core.feign.AuthServerFeign;
import uit.core.feign.MediaServiceFeign;
import uit.core.repository.LikeRepository;
import uit.core.repository.PostRepository;
import uit.core.util.SocialUtil;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PagedResourcesAssembler<Post> pagedResourcesAssembler;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private AuthServerFeign authServerFeign;

    @Autowired
    private MediaServiceFeign mediaServiceFeign;

    public PostResponse getAll(int page, int limit) {
        PostResponse postResponse = new PostResponse();
        Pageable paging = PageRequest.of(page, limit);
        Page<Post> response = postRepository.findAll(paging);

        List<PostItem> postItems = new ArrayList();
        for (Post post : response.getContent()) {
            PostItem postItem = modelMapper.map(post, PostItem.class);

            User user = authServerFeign.getById(post.getUserId());
            postItem.setUsername(user.getUsername());

            postItem.setTotalLike(getTotalLikes(postItem.getId()));

            postItem.setImages(getListImagesOfPost(postItem.getId()));
            
            postItem.setLiked(isLiked(postItem.getId()));
            postItems.add(postItem);
        }

        postResponse.setItems(postItems);
        if (page < response.getTotalPages()-1) {
            postResponse.setHasNext(true);
        } else {
            postResponse.setHasNext(false);
        }
        String nextLink = "/post?&page=".concat(String.valueOf(page+1));
        postResponse.setNextLink(nextLink);
        return postResponse;
    }

    private Boolean isLiked(long postId) {
        try {
            User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
            return likeRepository.findByUserIdAndPostId(user.getId(), postId).isPresent();
        } catch (Exception ex) {
            return false;
        }
    }

    private List<String> getListImagesOfPost(long postId) {
        List<String> result = new ArrayList<>();
        List<Image> images = mediaServiceFeign.getPostImages(postId);
        for (Image image : images) {
            result.add(image.getURL());
        }
        return result;
    }

    private long getTotalLikes(long postId) {
        return likeRepository.findAllByPostId(postId).size();
    }

    public PostItem getById(Long id) {
        Post post = postRepository.findById(id).get();
        PostItem postItem = modelMapper.map(post, PostItem.class);

        User user = authServerFeign.getById(post.getUserId());
        postItem.setUsername(user.getUsername());
        postItem.setImages(getListImagesOfPost(postItem.getId()));

        return postItem;
    }

    public PostItem create(Post post) {
        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        post.setUserId(user.getId());

        Post savedPost = postRepository.save(post);
        PostItem postResponse = modelMapper.map(savedPost, PostItem.class);
        postResponse.setUsername(user.getUsername());
        postResponse.setUserId(user.getId());
        return postResponse;
    }

    public Post update(Post post, Long id) {
        post.setId(id);
        return postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }


    public PostItem create(String title, String description, String typeBusiness, String typeProperty, String area, String district, String address, String roomNumber, String priceFrom, String priceTo, String expiredAt, MultipartFile[] images) throws ParseException {
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setTypeBusiness(Long.valueOf(typeBusiness));
        post.setTypeProperty(Long.valueOf(typeProperty));
        post.setArea(Long.valueOf(area));
        post.setDistrict(district);
        post.setAddress(address);
        post.setRoomNumber(Long.valueOf(roomNumber));
        post.setPriceFrom(Long.valueOf(priceFrom));
        post.setPriceTo(Long.valueOf(priceTo));

        Date expiredDate=new SimpleDateFormat("dd-MM-yyyy").parse(expiredAt);
        post.setExpiredAt(expiredDate);

        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        post.setUserId(user.getId());

        Post savedPost = postRepository.save(post);
        PostItem postResponse = modelMapper.map(savedPost, PostItem.class);
        postResponse.setUsername(user.getUsername());
        postResponse.setUserId(user.getId());

        List<String> imagesUrl = mediaServiceFeign.uploadMultipleFile(images, savedPost.getId());
        postResponse.setImages(imagesUrl);
        return postResponse;
    }
}
