package uit.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import uit.auth.entity.OauthClientDetails;
import uit.auth.repository.OauthClientDetailsRepository;

import java.util.List;

@Service
public class OauthClientDetailsServiceImpl implements OauthClientDetailsService {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    OauthClientDetailsRepository oauthClientDetailsRepository;

    @Override
    public void create(OauthClientDetails oauthClientDetails) {

        String hash = ENCODER.encode(oauthClientDetails.getClientSecret());
        oauthClientDetails.setClientSecret("{bcrypt}" + hash);

        oauthClientDetailsRepository.save(oauthClientDetails);
    }

    @Override
    public void deleteAll() {
        oauthClientDetailsRepository.deleteAll();
    }

    @Override
    public void createClients(List<OauthClientDetails> oauthClientDetailsList) {
        oauthClientDetailsList.forEach(oauthClientDetails -> {

            Assert.isTrue(isValidSecret(oauthClientDetails.getClientSecret()),
                    "Invalid clientSecret: " + oauthClientDetails.getClientSecret());

            String hash = ENCODER.encode(oauthClientDetails.getClientSecret());
            oauthClientDetails.setClientSecret("{bcrypt}" + hash);
        });

        oauthClientDetailsRepository.saveAll(oauthClientDetailsList);
    }

    private boolean isValidSecret(String secret) {
        return !StringUtils.isEmpty(secret) ? true : false;
    }

    @Override
    public OauthClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return oauthClientDetailsRepository.findById(clientId)
                .orElseThrow(() -> new ClientRegistrationException("No client with requested id: " + clientId));
    }
}
