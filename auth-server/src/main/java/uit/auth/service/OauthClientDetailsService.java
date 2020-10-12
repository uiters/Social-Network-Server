package uit.auth.service;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import uit.auth.entity.OauthClientDetails;

import java.util.List;

public interface OauthClientDetailsService extends ClientDetailsService {
    void create(OauthClientDetails oauthClientDetails);

    void createClients(List<OauthClientDetails> oauthClientDetails);

    void deleteAll();
}
