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
import org.springframework.util.StringUtils;
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
import uit.core.repository.specification.PostSpecification;
import uit.core.repository.specification.SearchCriteria;
import uit.core.repository.specification.SearchOperation;
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

    public PostResponse search(int page, int limit, String title, String description, String typeBusiness, String typeProperty, String area, String district, String address, String roomNumber, String priceFrom, String priceTo, String expiredAt, String price) {
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

        return searchAll(page, limit, postSpecification);

    }

    public PostResponse searchAll(int page, int limit, PostSpecification postSpecification) {
        PostResponse postResponse = new PostResponse();
        Pageable paging = PageRequest.of(page, limit);
        Page<Post> response = postRepository.findAll(postSpecification, paging);

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
}
