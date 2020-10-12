package uit.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;
import uit.auth.entity.OauthClientDetails;
import uit.auth.service.OauthClientDetailsService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Configuration
public class AlwaysProfileConfig implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlwaysProfileConfig.class);

    @Autowired
    private OauthClientDetailsService oauthClientDetailsService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Environment env;

    @Override
    public void run(String... arg0) throws Exception {
        generateInternalClients();
    }

    /**
     * Generates and stores internal clients into the datastore.
     *
     * @throws IOException
     *
     */
    private void generateInternalClients() throws IOException {
        oauthClientDetailsService.deleteAll();

        List<Map<String, Object>> clients = getClientCredentialsFromFile(env.getProperty("social.security.clientCredentialsFile"));

        for (Map<String, Object> map : clients) {

            OauthClientDetails oauthClientDetails = OauthClientDetails.builder()
                    .withClientId((String) map.get("clientid"))
                    .withScope((String) map.get("scopes"))
                    .withAccessTokenValiditySeconds((Integer) map.get("accessTokenValiditySeconds"))
                    .withAuthorizedGrantTypes((String) map.get("grantTypes"))
                    .withClientSecret(obtainSecret((String) map.get("clientsecret")))
                    .build();
            oauthClientDetailsService.create(oauthClientDetails);
        }

    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getClientCredentialsFromFile(String filePath) throws IOException {
        Yaml yaml = new Yaml();

        if (filePath.startsWith("/")) {
            filePath = "file:" + filePath;
        } else {
            filePath = "classpath:" + filePath;
        }

        LOGGER.info("About to load file " + filePath + " with client credentials");

        Resource resource = resourceLoader.getResource(filePath);

        InputStream inputStream = resource.getInputStream();

        Map<String, Object> obj = yaml.load(inputStream);

        return (List<Map<String, Object>>) obj.get("clients");
    }

    /**
     * Retrieves the secret value. If it is defined as environmental value "${VALUE}", it will
     * look into the system env values. Otherwise it will use the raw value. In case of null,
     * it is initialized as an empty String.
     *
     * @param clientName
     * @param isBrowser
     * @return
     */
    private static String obtainSecret(String secretValue) {

        if (StringUtils.isEmpty(secretValue)) {
            secretValue = new String();
        } else if (!StringUtils.isEmpty(secretValue) && secretValue.startsWith("${") && secretValue.endsWith("}")) {
            String envName = secretValue.substring(2, secretValue.length() - 1);
            secretValue = System.getenv(envName);
            if (StringUtils.isEmpty(secretValue)) {
                throw new RuntimeException(String.format("Environment variable %s was not found.", envName));
            }
        }
        return secretValue;
    }
}
