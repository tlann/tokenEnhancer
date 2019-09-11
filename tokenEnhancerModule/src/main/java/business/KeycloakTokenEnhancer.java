package business;

import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.AccessToken;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeycloakTokenEnhancer extends AbstractOIDCProtocolMapper implements OIDCAccessTokenMapper, OIDCIDTokenMapper, UserInfoTokenMapper {

    public static final String PROVIDER_ID = "oidc-token-enhancer-mapper";

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    @PersistenceContext(unitName = "UserPU")
    protected EntityManager entityManager;

    static {
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, KeycloakTokenEnhancer.class);
    }

    @Override
    public AccessToken transformAccessToken(AccessToken accessToken, ProtocolMapperModel protocolMapperModel, KeycloakSession keycloakSession, UserSessionModel userSessionModel, ClientSessionContext clientSessionContext) {
        System.out.println("++++++++++++++++++++++++++++++++");

        if (entityManager == null ) {
            System.out.println("entityManager is null ");
        }


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("UserPU");

        if (entityManagerFactory == null ) {
            System.out.println("entityManagerFactory is null ");
        }
        else {
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();
            if (entityManager2 == null) {
                System.out.println("entityManager2 is null ");
            }
        }


        System.out.println("++++++++++++++++++++++++++++++++");

        accessToken.getOtherClaims().put("fruit", "pear, apple, tangerine");
        return accessToken;
    }

    @Override
    public String getDisplayCategory() {
        return "Token Enhancer";
    }

    @Override
    public String getDisplayType() {
        return "Token Enhancer";
    }

    @Override
    public String getHelpText() {
        return "Add to claims for the User Service";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    public static ProtocolMapperModel create(String name, boolean accessToken, boolean idToken, boolean userInfo) {
        ProtocolMapperModel mapper = new ProtocolMapperModel();
        mapper.setName(name);
        mapper.setProtocolMapper(PROVIDER_ID);
        mapper.setProtocol(OIDCLoginProtocol.LOGIN_PROTOCOL);
        Map<String, String> config = new HashMap<String, String>();
        if (accessToken) config.put(OIDCAttributeMapperHelper.INCLUDE_IN_ACCESS_TOKEN, "true");
        if (idToken) config.put(OIDCAttributeMapperHelper.INCLUDE_IN_ID_TOKEN, "true");
        if (userInfo) config.put(OIDCAttributeMapperHelper.INCLUDE_IN_USERINFO, "true");
        mapper.setConfig(config);

        return mapper;
    }


}
