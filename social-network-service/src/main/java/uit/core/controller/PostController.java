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
import uit.core.service.PostService;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
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
    public PostItem create(@RequestPart String title,
                           @RequestPart String description,
                           @RequestPart String typeBusiness,
                           @RequestPart String typeProperty,
                           @RequestPart String area,
                           @RequestPart String district,
                           @RequestPart String address,
                           @RequestPart String roomNumber,
                           @RequestPart String priceFrom,
                           @RequestPart String priceTo,
                           @RequestPart String expiredAt,
                           @RequestPart(value = "images") MultipartFile[] images) throws ParseException {
        return postService.create(title, description, typeBusiness, typeProperty, area, district, address, roomNumber, priceFrom, priceTo, expiredAt, images);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/{id}")
    public Post update(@RequestBody Post post, @PathVariable Long id) {
        return postService.update(post, id);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        postService.deleteById(id);
    }
}
