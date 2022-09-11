package com.dailywords.edgeservice.runner;


import com.dailywords.edgeservice.config.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class KeycloakInitializerRunner implements CommandLineRunner {
    private static final String KEYCLOAK_SERVER_URL = "http://localhost:8080";
    private static final String DAILY_WORDS_REALM_NAME = "daily-words";
    private static final String EDGE_SERVICE_CLIENT_ID = "edge-service";
    private static final String EDGE_SERVICE_REDIRECT_URL = "http://localhost:10002/*";
    private static final List<UserPass> EDGE_SERVICE_USERS = Arrays.asList(
            new UserPass("admin", "admin"),
            new UserPass("user", "user")
    );
    private final Keycloak keycloakAdmin;

    @Override
    public void run(String... args) {
        log.info("Initializing '{}' realm in Keycloak...", DAILY_WORDS_REALM_NAME);

        Optional<RealmRepresentation> representationOptional = keycloakAdmin.realms()
                .findAll()
                .stream()
                .filter(r -> r.getRealm().equals(DAILY_WORDS_REALM_NAME))
                .findAny();

        if (representationOptional.isPresent()) {
            log.info("realm {} already exists", DAILY_WORDS_REALM_NAME);
            return;
        }

        // Realm
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(DAILY_WORDS_REALM_NAME);
        realmRepresentation.setEnabled(true);
        realmRepresentation.setRegistrationAllowed(true);

        //Client
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(EDGE_SERVICE_CLIENT_ID);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setRedirectUris(Collections.singletonList(EDGE_SERVICE_REDIRECT_URL));
        clientRepresentation.setDefaultRoles(new String[]{WebSecurityConfig.USER});

        realmRepresentation.setClients(Collections.singletonList(clientRepresentation));

        List<UserRepresentation> userRepresentations = EDGE_SERVICE_USERS.stream()
                .map(userPass -> {
                    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                    credentialRepresentation.setValue(userPass.username());

                    // User
                    UserRepresentation userRepresentation = new UserRepresentation();
                    userRepresentation.setUsername(userPass.username());
                    userRepresentation.setEnabled(true);
                    userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
                    userRepresentation.setClientRoles(getClientRoles(userPass));
                    return userRepresentation;
                }).toList();

       realmRepresentation.setUsers(userRepresentations);

       // Create Realm
        keycloakAdmin.realms().create(realmRepresentation);

        // Testing
        UserPass admin = EDGE_SERVICE_USERS.get(0);
        log.info("Testing getting token for '{}' ...", admin.username());

        Keycloak edgeService = KeycloakBuilder.builder().serverUrl(KEYCLOAK_SERVER_URL)
                .realm(DAILY_WORDS_REALM_NAME)
                .username(admin.username())
                .password(admin.password())
                .clientId(EDGE_SERVICE_CLIENT_ID)
                .build();

        log.info("'{}' token: {}", admin.username(), edgeService.tokenManager().grantToken().getToken());
        log.info("'{}' initialization completed successfully!", DAILY_WORDS_REALM_NAME);
    }

    public Map<String, List<String>> getClientRoles(UserPass userPass) {
        List<String> roles = new ArrayList<>();
        roles.add(WebSecurityConfig.USER);

        if ("admin".equals(userPass.username())) {
            roles.add(WebSecurityConfig.EDGE_SERVICE_MANAGER);
        }
        return Map.of(EDGE_SERVICE_CLIENT_ID, roles);
    }

    private record UserPass(String username, String password) {
    }
}