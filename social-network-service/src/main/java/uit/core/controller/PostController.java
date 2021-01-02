package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.core.dto.response.PostItem;
import uit.core.dto.response.PostResponse;
import uit.core.entity.Post;
import uit.core.entity.UserPost;
import uit.core.service.PostService;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/post")
public class    PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public PostResponse getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int limit) {
        return postService.getAll(page, limit);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @GetMapping("/{id}")
    public PostItem getById(@PathVariable Long id) {
        return postService.getById(id);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PostMapping
    public PostItem create(@RequestParam(defaultValue = "", required = false) String title,
                           @RequestParam(defaultValue = "", required = false) String description,
                           @RequestParam(defaultValue = "", required = false) String typeBusiness,
                           @RequestParam(defaultValue = "", required = false) String typeProperty,
                           @RequestParam(defaultValue = "", required = false) String area,
                           @RequestParam(defaultValue = "", required = false) String district,
                           @RequestParam(defaultValue = "", required = false) String address,
                           @RequestParam(defaultValue = "", required = false) String roomNumber,
                           @RequestParam(defaultValue = "", required = false) String priceFrom,
                           @RequestParam(defaultValue = "", required = false) String priceTo,
                           @RequestParam(defaultValue = "", required = false) String price,
                           @RequestParam(defaultValue = "", required = false) String expiredAt,
                           @RequestPart(value = "images") MultipartFile[] images) throws ParseException {
        return postService.create(title, description, typeBusiness, typeProperty, area, district, address, roomNumber, priceFrom, priceTo, expiredAt, price, images);
    }

    @GetMapping("/search")
    public PostResponse search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "", required = false) String title,
            @RequestParam(defaultValue = "", required = false) String description,
            @RequestParam(defaultValue = "", required = false) String typeBusiness,
            @RequestParam(defaultValue = "", required = false) String typeProperty,
            @RequestParam(defaultValue = "", required = false) String area,
            @RequestParam(defaultValue = "", required = false) String district,
            @RequestParam(defaultValue = "", required = false) String address,
            @RequestParam(defaultValue = "", required = false) String roomNumber,
            @RequestParam(defaultValue = "", required = false) String priceFrom,
            @RequestParam(defaultValue = "", required = false) String priceTo,
            @RequestParam(defaultValue = "", required = false) String price,
            @RequestParam(defaultValue = "", required = false) String expiredAt,
            @RequestParam(defaultValue = "", required = false) String userId
            ) throws ParseException {
        return postService.search(page,limit, title, description, typeBusiness, typeProperty, area, district, address, roomNumber, priceFrom, priceTo, expiredAt, price, userId);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/{id}")
    public PostItem update(@RequestBody Post post, @PathVariable Long id) {
        return postService.update(post, id);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        postService.deleteById(id);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @GetMapping("/savedPost")
    public List<PostItem> getSavedPost() throws Exception {
        return postService.getSavedPost();
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PostMapping("/save/{postId}")
    public UserPost savePostForLater(@PathVariable long postId) {
        return postService.savePostForLater(postId);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PostMapping("/delete/{postId}")
    public String deleteSavedPost(@PathVariable long postId) throws Exception {
        return postService.deleteSavedPost(postId);
    }
}
