package uit.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import uit.core.dto.response.PostItem;
import uit.core.dto.response.PostResponse;
import uit.core.entity.Post;
import uit.core.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PagedResourcesAssembler<Post> pagedResourcesAssembler;

    public PostResponse getAll(int page, int limit) {
        PostResponse postResponse = new PostResponse();
        Pageable paging = PageRequest.of(page, limit);
        Page<Post> response = postRepository.findAll(paging);

        List<PostItem> postItems = new ArrayList();
        for (Post post : response.getContent()) {
            PostItem postItem = modelMapper.map(post, PostItem.class);
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

    public PostItem getById(Long id) {
        Post post = postRepository.findById(id).get();
        return modelMapper.map(post, PostItem.class);
    }

    public PostItem create(Post post) {
        Post savedPost = postRepository.save(post);
        PostItem postResponse = modelMapper.map(savedPost, PostItem.class);
        postResponse.setUsername("Huynh Tan Duy");
        postResponse.setUserId(1);
        return postResponse;
    }

    public Post update(Post post, Long id) {
        post.setId(id);
        return postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public Page<Post> getAllTest(int page, int limit) {
        Pageable paging = PageRequest.of(page, limit);
        Page<Post> pagedResult = postRepository.findAll(paging);
        return pagedResult;
    }
}
