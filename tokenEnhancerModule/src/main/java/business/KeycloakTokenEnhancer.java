package business;

import mil.noms.domain.user.models.v1.User;
import mil.noms.repository.user.repositories.v1.ReadOnlyUserRepository;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.AccessToken;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeycloakTokenEnhancer extends AbstractOIDCProtocolMapper implements OIDCAccessTokenMapper, OIDCIDTokenMapper, UserInfoTokenMapper {

    public static final String PROVIDER_ID = "oidc-token-enhancer-mapper";

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, KeycloakTokenEnhancer.class);
    }

    @Override
    public AccessToken transformAccessToken(AccessToken accessToken, ProtocolMapperModel protocolMapperModel, KeycloakSession keycloakSession, UserSessionModel userSessionModel, ClientSessionContext clientSessionContext) {
        System.out.println("++++++++++++++++++++++++++++++++");
        ReadOnlyUserRepository readOnlyUserRepository = BeanUtil.getBean(ReadOnlyUserRepository.class);
        List<User> all = readOnlyUserRepository.findAll();
        System.out.printf( "This many %d\n",all.size() );
        System.out.println("++++++++++++++++++++++++++++++++");

        accessToken.getOtherClaims().put("fruit", "pear, apple, tangerine");
        return accessToken;
    }

    @Override
    public String getDisplayCategory() {
        return "NOMS Token Enhancer";
    }

    @Override
    public String getDisplayType() {
        return "NOMS Token Enhancer";
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
