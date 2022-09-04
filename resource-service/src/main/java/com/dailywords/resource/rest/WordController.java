package com.dailywords.resource.rest;


import com.dailywords.resource.domain.entity.Word;
import com.dailywords.resource.repository.WordRepository;
import lombok.AllArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.RealmRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequestMapping("/api/v1/words")
@AllArgsConstructor
@RestController
public class WordController {
    private final WordRepository wordRepository;
    private static final Logger log = LoggerFactory.getLogger(WordController.class);

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_TEST')")
    public ResponseEntity<List<Word>> getAllWords(Principal principal) {
        Keycloak keycloak = Keycloak.getInstance("http://localhost:8080", "resource_server", "admin", "Pa55w0rd", "resource_server");
        RealmRepresentation realm = keycloak.realm("resource_server").toRepresentation();

        log.info("USERS: {}", realm.getUsers());


        log.info("[PRINCIPAL: {}]", principal);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        log.info("AUTHENTICATION: {}", authentication);
        return ResponseEntity.ok(wordRepository.findAll());
    }
}
