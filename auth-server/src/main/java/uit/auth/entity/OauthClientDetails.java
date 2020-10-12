package uit.auth.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "oauth_client_details")
public class OauthClientDetails implements ClientDetails {
    private static final long serialVersionUID = -2640719988642673051L;

    @Id
    private String clientId;
    private String resourceIds;
    private String clientSecret;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoapprove;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;

    public OauthClientDetails() {
    }

    OauthClientDetails(String clientId, String clientSecret, Integer accessTokenValiditySeconds, String scope,
                       String authorizedGrantTypes) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.scope = scope;
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Set<String> getResourceIds() {
        return stringToSet(resourceIds);
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = String.join(",", resourceIds);
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Set<String> getScope() {
        return stringToSet(scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Set<String> getAuthorizedGrantTypes() {
        return stringToSet(authorizedGrantTypes);
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<String, Object>();
    }

    public void setAdditionalInformation(String additionalInformacion) {
        this.additionalInformation = additionalInformacion;
    }

    public String getAutoapprove() {
        return autoapprove;
    }

    public void setAutoapprove(String autoapprove) {
        this.autoapprove = autoapprove;
    }

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    @Override
    public boolean isSecretRequired() {
        return false;
    }

    @Override
    public boolean isScoped() {
        return false;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return null;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public String toString() {
        return "OauthClientDetails [clientId=" + clientId + ", resourceIds=" + resourceIds + ", scope=" + scope
                + ", authorizedGrantTypes=" + authorizedGrantTypes + ", webServerRedirectUri=" + webServerRedirectUri
                + ", authorities=" + authorities + ", accessTokenValidity=" + accessTokenValidity
                + ", refreshTokenValidity=" + refreshTokenValidity + ", additionalInformation=" + additionalInformation
                + ", autoapprove=" + autoapprove + ", accessTokenValiditySeconds=" + accessTokenValiditySeconds
                + ", refreshTokenValiditySeconds=" + refreshTokenValiditySeconds + "]";
    }

    /**
     * Returns an instance of OauthClientDetailsBuilder for creating the OauthClientDetails object
     *
     * @return
     */
    public static OauthClientDetailsBuilder builder() {
        return new OauthClientDetailsBuilder();
    }

    private static Set<String> stringToSet(String value) {
        Set<String> set = new HashSet<String>();
        if (!StringUtils.isEmpty(value)) {
            set = Set.of(value.replace(" ", "").split(","));
        }
        return set;
    }
}
