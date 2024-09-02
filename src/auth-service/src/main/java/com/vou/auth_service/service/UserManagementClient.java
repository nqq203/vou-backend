package com.vou.auth_service.service;

import com.vou.auth_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementClient {

    private final RestTemplate restTemplate;
    private final String userUrl = "http://user-service:8082/api/v1/users";
    private final String playerUrl = "http://user-service:8082/api/v1/players";
    private final String brandUrl = "http://user-service:8082/api/v1/brands";
    private final String adminUrl = "http://user-service:8082/api/v1/admins";
    private final String sessionUrl = "http://user-service:8082/api/v1/sessions";


    @Autowired
    public UserManagementClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

//    public User createUser(User user) {
//        return restTemplate.postForObject(userServiceUrl, user, User.class);
//    }

    public Boolean createAdmin(Admin admin) {
        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity(adminUrl, admin, Boolean.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                System.err.println("Failed to create admin, status code: " + response.getStatusCode());
                return false;
            }
        } catch (RestClientException e) {
            System.err.println("RestClientException when creating admin: " + e.getMessage());
            return false;
        }
    }

    public Boolean createBrand(Brand brand) {
        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity(brandUrl, brand, Boolean.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                System.err.println("Failed to create brand, status code: " + response.getStatusCode());
                return false;
            }
        } catch (RestClientException e) {
            System.err.println("RestClientException when creating brand: " + e.getMessage());
            return false;
        }
    }

    public Long createPlayer(Player player) {
        try {
            ResponseEntity<Long> response = restTemplate.postForEntity(playerUrl + "/", player, Long.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                System.err.println("Failed to create player, status code: " + response.getStatusCode());
                return null;
            }
        } catch (RestClientException e) {
            System.err.println("RestClientException when creating player: " + e.getMessage());
            return null;
        }
    }

    //TODO: re-handle for it 13-8-2024
    public User updateUserInternal(User user) {
        try {
            HttpEntity<User> requestEntity = new HttpEntity<>(user);
            ResponseEntity<User> response = restTemplate.exchange(
                    userUrl + "/" + user.getIdUser(),
                    HttpMethod.PUT,
                    requestEntity,
                    User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            else {
                System.err.println("Failed to update user, status code: " + response.getStatusCode());
                return null;
            }
        } catch (RestClientException e) {
            System.err.println("RestClientException when updating player: " + e.getMessage());
            return null;
        }
    }


    public Optional<User> getUserByIdentifier(String identifier) {
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(userUrl + "/" + identifier, User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error retrieving user: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Object> getPlayerByIdUser(Long userId) {
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(playerUrl + "/" + userId, Object.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error retrieving player: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Object> getAdminByIdUser(Long userId) {
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(adminUrl + "/" + userId, Object.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error retrieving admin: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Object> getBrandByIdUser(Long userId) {
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(brandUrl + "/" + userId, Object.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error retrieving brand: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Session createSession(Session session) {
        return restTemplate.postForObject(sessionUrl + "/", session, Session.class);
    }

    public Session getSessionByToken(String token) {
        try {
            ResponseEntity<Session> response = restTemplate.getForEntity(sessionUrl + "/" + token, Session.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error retrieving session: " + e.getMessage());
            return null;
        }
    }

    public Session updateSession(Session session) {
//        return restTemplate.postForObject(userServiceUrl + "/session/" + session.getToken(), session, Session.class);
        try {
            HttpEntity<Session> requestEntity = new HttpEntity<>(session);
            ResponseEntity<Session> response = restTemplate.exchange(
                    sessionUrl + "/" + session.getIdSession(),
                    HttpMethod.PUT,
                    requestEntity,
                    Session.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                System.err.println("Failed to update session, status code: " + response.getStatusCode());
                return null;
            }
        } catch (RestClientException e) {
            System.err.println("RestClientException when updating session: " + e.getMessage());
            return null;
        }
    }

    public Optional<List<Session>> getListSession() {
        try {
            ResponseEntity<List<Session>> response = restTemplate.exchange(
                    sessionUrl + "/",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Session>>() {}
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error retrieving sessions: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<User> getUserByUsernameAndEmail(String username, String email){
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(userUrl + "/query" +"?username=" + username + "&email=" + email, User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("Error in getUserByUsernameAndEmail: " + e.getMessage());
            return Optional.empty();
        }
    }
}
