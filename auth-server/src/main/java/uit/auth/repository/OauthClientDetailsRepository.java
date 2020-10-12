package uit.auth.repository;


import org.springframework.data.repository.CrudRepository;
import uit.auth.entity.OauthClientDetails;

public interface OauthClientDetailsRepository extends CrudRepository<OauthClientDetails, String> {
}
