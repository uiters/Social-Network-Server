package uit.auth.entity;

public class OauthClientDetailsBuilder {
    private String clientId;
    private String clientSecret;
    private String scope;
    private String authorizedGrantTypes;
    private Integer accessTokenValiditySeconds;

    OauthClientDetailsBuilder() {}

    public OauthClientDetailsBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public OauthClientDetailsBuilder withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public OauthClientDetailsBuilder withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public OauthClientDetailsBuilder withAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
        return this;
    }

    public OauthClientDetailsBuilder withAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        return this;
    }

    public OauthClientDetails build() {
        return new OauthClientDetails(clientId, clientSecret, accessTokenValiditySeconds, scope, authorizedGrantTypes);
    }
}
