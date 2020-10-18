package uit.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uit.core.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
//    Page<Post> findAll(Specification<T> postSpecification, Pageable paging);
//    Page<Post> findAll(Pageable pageable);
}
