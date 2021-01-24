package uit.auth.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import uit.auth.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {
    private List<SearchCriteria> list;

    public UserSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : list) {
            if (root.get(criteria.getKey()).getJavaType().equals(String.class)) {
                predicates.add(builder.like(
                        root.get(criteria.<String>getKey()), "%" + criteria.getValue().toString() + "%"));
            }
            else {
                predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
