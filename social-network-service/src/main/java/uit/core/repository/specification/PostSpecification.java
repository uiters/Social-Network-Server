package uit.core.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import uit.core.entity.Post;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification implements Specification<Post> {
    private List<SearchCriteria> list;

    public PostSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : list) {
                if (root.get(criteria.getKey()).getJavaType().equals(String.class)) {
                    predicates.add(builder.like(
                            root.get(criteria.<String>getKey()), "%" + criteria.getValue().toString() + "%"));
                } else {

                        if (criteria.getKey().equals("priceFrom")) {
                            if (isSeller(list)) {
                                predicates.add(builder.greaterThanOrEqualTo(root.<Long>get("price"), Long.valueOf(criteria.getValue().toString())));
                            } else {
                                predicates.add(builder.greaterThanOrEqualTo(root.<Long>get("priceFrom"), Long.valueOf(criteria.getValue().toString())));
                            }
                        } else if (criteria.getKey().equals("priceTo")) {
                            if (isSeller(list)) {
                                predicates.add(builder.lessThanOrEqualTo(root.<Long>get("price"), Long.valueOf(criteria.getValue().toString())));
                            } else {
                                predicates.add(builder.lessThanOrEqualTo(root.<Long>get("priceTo"), Long.valueOf(criteria.getValue().toString())));
                            }
                        } else {
                            predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                        }

                }
            }


        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private boolean isSeller(List<SearchCriteria> list) {
        for (SearchCriteria criteria : list) {
            if (criteria.getKey().equals("typeBusiness")) {
                if (criteria.getValue().equals("1") || criteria.getValue().equals("3")) return true;
            }
        }
        return false;
    }
}
