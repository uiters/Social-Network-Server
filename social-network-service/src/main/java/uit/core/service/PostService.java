package uit.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;

import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uit.core.dto.response.PostItem;
import uit.core.dto.response.PostResponse;
import uit.core.entity.Image;
import uit.core.entity.Post;
import uit.core.entity.User;
import uit.core.entity.UserPost;
import uit.core.entity.event.UserAction;
import uit.core.event.Action;
import uit.core.event.CareEvent;
import uit.core.feign.AuthServerFeign;
import uit.core.feign.MediaServiceFeign;
import uit.core.repository.LikeRepository;
import uit.core.repository.PostRepository;
import uit.core.repository.UserPostRepository;
import uit.core.repository.event.UserActionRepository;
import uit.core.repository.specification.PostSpecification;
import uit.core.repository.specification.SearchCriteria;
import uit.core.repository.specification.SearchOperation;
import uit.core.util.SocialUtil;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private static final ModelMapper modelMapper = new ModelMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

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

    @Autowired
    private UserPostRepository userPostRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private UserActionRepository userActionRepository;

    public PostResponse getAll(int page, int limit) {
        PostResponse postResponse = new PostResponse();
        Pageable paging = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> response = postRepository.findAll(paging);

        List<PostItem> postItems = new ArrayList();
        for (Post post : response.getContent()) {
            PostItem postItem = populatePostResponse(post);

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
        postItem.setTotalLike(getTotalLikes(postItem.getId()));
        postItem.setLiked(isLiked(postItem.getId()));
        postItem.setAvatar(user.getAvatar());
        return postItem;
    }

    public PostItem create(Post post) {
        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        post.setUserId(user.getId());

        Post savedPost = postRepository.save(post);
        PostItem postResponse = modelMapper.map(savedPost, PostItem.class);
        postResponse.setUsername(user.getUsername());
        postResponse.setUserId(user.getId());
        postResponse.setAvatar(user.getAvatar());
        return postResponse;
    }

    public PostItem update(Post post, Long id) {
        Post postDb = postRepository.findById(id).get();
        post.setId(id);
        post.setCreatedAt(postDb.getCreatedAt());

        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        post.setUserId(user.getId());

        Post savedPost = postRepository.save(post);

        PostItem postResponse = modelMapper.map(savedPost, PostItem.class);
        postResponse.setUsername(user.getUsername());
        postResponse.setUserId(user.getId());
        postResponse.setImages(getListImagesOfPost(postResponse.getId()));
        postResponse.setTotalLike(getTotalLikes(postResponse.getId()));
        postResponse.setLiked(isLiked(postResponse.getId()));
        postResponse.setAvatar(user.getAvatar());
        return postResponse;
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }


    public PostItem create(String title, String description, String typeBusiness, String typeProperty, String area, String district, String address, String roomNumber, String priceFrom, String priceTo, String expiredAt, String price, MultipartFile[] images) throws ParseException {
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        if (!StringUtils.isEmpty(typeBusiness)) {
            post.setTypeBusiness(Long.valueOf(typeBusiness));
        }
        if (!StringUtils.isEmpty(typeProperty)) {
            post.setTypeProperty(Long.valueOf(typeProperty));
        }
        if (!StringUtils.isEmpty(area)) {
            post.setArea(Long.valueOf(area));
        }
        post.setDistrict(district);
        post.setAddress(address);
        if (!StringUtils.isEmpty(roomNumber)) {
            post.setRoomNumber(Long.valueOf(roomNumber));
        }
        if (!StringUtils.isEmpty(priceFrom)) {
            post.setPriceFrom(Long.valueOf(priceFrom));
        }

        if (!StringUtils.isEmpty(price)) {
            post.setPrice(Long.valueOf(price));
        }

        if (!StringUtils.isEmpty(priceTo)) {
            post.setPriceTo(Long.valueOf(priceTo));
        }

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

    public PostResponse search(int page, int limit, String title, String description, String typeBusiness, String typeProperty, String area, String district, String address, String roomNumber, String priceFrom, String priceTo, String expiredAt, String price, String userId) {
        PostSpecification postSpecification = new PostSpecification();
        if (!StringUtils.isEmpty(title)) {
            postSpecification.add(new SearchCriteria("title", title, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(description)) {
            postSpecification.add(new SearchCriteria("description", description, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(typeBusiness)) {
           postSpecification.add(new SearchCriteria("typeBusiness", typeBusiness, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(typeProperty)) {
            postSpecification.add(new SearchCriteria("typeProperty", typeProperty, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(area)) {
            postSpecification.add(new SearchCriteria("area", area, SearchOperation.LIKE));
        }if (!StringUtils.isEmpty(district)) {
            postSpecification.add(new SearchCriteria("district", district, SearchOperation.LIKE));
        }if (!StringUtils.isEmpty(address)) {
            postSpecification.add(new SearchCriteria("address", address, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(roomNumber)) {
            postSpecification.add(new SearchCriteria("roomNumber", roomNumber, SearchOperation.LIKE));
        }if (!StringUtils.isEmpty(priceFrom)) {
            postSpecification.add(new SearchCriteria("priceFrom", priceFrom, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(priceTo)) {
            postSpecification.add(new SearchCriteria("priceTo", priceTo, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(expiredAt)) {
            postSpecification.add(new SearchCriteria("expiredAt", expiredAt, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(price)) {
            postSpecification.add(new SearchCriteria("price", price, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(userId)) {
            postSpecification.add(new SearchCriteria("userId", userId, SearchOperation.LIKE));
        }

        return searchAll(page, limit, postSpecification);

    }

    public PostResponse searchAll(int page, int limit, PostSpecification postSpecification) {
        PostResponse postResponse = new PostResponse();
        Pageable paging = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> response = postRepository.findAll(postSpecification, paging);

        List<PostItem> postItems = new ArrayList();
        for (Post post : response.getContent()) {
            PostItem postItem = modelMapper.map(post, PostItem.class);

            User user = authServerFeign.getById(post.getUserId());
            postItem.setUsername(user.getUsername());

            postItem.setTotalLike(getTotalLikes(postItem.getId()));

            postItem.setImages(getListImagesOfPost(postItem.getId()));

            postItem.setLiked(isLiked(postItem.getId()));

            postItem.setAvatar(user.getAvatar());

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

    public UserPost savePostForLater(long postId) {
        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        Optional<UserPost> userPostOptional = userPostRepository.findByPostIdAndUserId(postId, user.getId());
        if (userPostOptional.isPresent()) {
            LOGGER.info("Post was already saved by user before");
            return userPostOptional.get();
        }

        UserPost userPost = new UserPost();
        userPost.setPostId(postId);
        userPost.setUserId(user.getId());

        publishEvent(userPost);

        return userPostRepository.save(userPost);
    }

    private void publishEvent(UserPost userPost) {
        UserAction userAction = new UserAction();
        userAction.setUserId(userPost.getUserId());
        userAction.setActionId(Action.SAVE_POST.getCode());
        userAction.setPostId(userPost.getPostId());

        publisher.publishEvent(new CareEvent(this, userAction));
        userActionRepository.save(userAction);
    }

    public String deleteSavedPost(long postId) throws Exception {
        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        Optional<UserPost> userPostOptional = userPostRepository.findByPostIdAndUserId(postId, user.getId());
        if (userPostOptional.isEmpty()) {
            LOGGER.info("No saved post found for userId and postId");
            throw new Exception("Saved Post not found with userId and postId");
        }
        userPostRepository.deleteById(userPostOptional.get().getId());
        return "Delete saved post successfully";
    }

    public List<PostItem> getSavedPost() throws Exception {
        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        List<UserPost> userPosts = userPostRepository.findAllByUserId(user.getId());

        List<PostItem> posts = new ArrayList<>();
        for (UserPost userPost : userPosts) {
            Optional<Post> post = postRepository.findById(userPost.getPostId());
            if (!post.isPresent()) {
                continue;
            }
            PostItem postItem = populatePostResponse(post.get());

            posts.add(postItem);
        }

        return posts;


    }

    private PostItem populatePostResponse(Post post) {
            PostItem postItem = modelMapper.map(post, PostItem.class);

            User user = authServerFeign.getById(post.getUserId());
            postItem.setUsername(user.getUsername());

            postItem.setTotalLike(getTotalLikes(postItem.getId()));

            postItem.setImages(getListImagesOfPost(postItem.getId()));

            postItem.setLiked(isLiked(postItem.getId()));

            postItem.setAvatar(user.getAvatar());

            return postItem;

    }
}
