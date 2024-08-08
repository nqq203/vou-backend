package com.vou.auth_service.service;

import com.vou.auth_service.model.*;
import org.apache.coyote.Response;
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
    private final String userServiceUrl = "http://user-service:8082/api/v1/user";

    @Autowired
    public UserManagementClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

//    public User createUser(User user) {
//        return restTemplate.postForObject(userServiceUrl, user, User.class);
//    }

    public Boolean createAdmin(Admin admin) {
        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity(userServiceUrl + "/admin", admin, Boolean.class);
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
            ResponseEntity<Boolean> response = restTemplate.postForEntity(userServiceUrl + "/brand", brand, Boolean.class);
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

    public Boolean createPlayer(Player player) {
        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity(userServiceUrl + "/player", player, Boolean.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                System.err.println("Failed to create player, status code: " + response.getStatusCode());
                return false;
            }
        } catch (RestClientException e) {
            System.err.println("RestClientException when creating player: " + e.getMessage());
            return false;
        }
    }

//    note lai o day chut /create-player
    public Boolean updateUserInternal(User user) {
        try {
            HttpEntity<User> requestEntity = new HttpEntity<>(user);
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    userServiceUrl + "/" + user.getIdUser() + "/internal-update",
                    HttpMethod.PUT,
                    requestEntity,
                    Boolean.class);
//            ResponseEntity<Boolean> response = restTemplate.postForEntity(userServiceUrl + "/" + user.getIdUser() + "/internal-update", user, Boolean.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            }
            else {
                System.err.println("Failed to update user, status code: " + response.getStatusCode());
                return false;
            }
        } catch (RestClientException e) {
            System.err.println("RestClientException when creating player: " + e.getMessage());
            return false;
        }
    }


    public Optional<User> getUserByUsername(String username) {
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(userServiceUrl + "/by-username/" + username, User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();  // When user is not found or any other non-OK response
        } catch (Exception e) {
            System.err.println("Error retrieving user: " + e.getMessage());
            return Optional.empty();  // Return empty Optional in case of exceptions
        }
    }

//    public Optional<Player> getPlayerByIdUser(Long userId) {
//        try {
//            ResponseEntity<Player> response = restTemplate.getForEntity(userServiceUrl + "/get-player-by-userid/" + userId, Player.class);
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return Optional.ofNullable(response.getBody());
//            }
//            return Optional.empty();  // When user is not found or any other non-OK response
//        } catch (Exception e) {
//            System.err.println("Error retrieving user: " + e.getMessage());
//            return Optional.empty();  // Return empty Optional in case of exceptions
//        }
//    }

    public Optional<User> getUserByEmail(String email) {
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(userServiceUrl + "/by-email/" + email, User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable((response.getBody()));
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error retrieving user: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Session createSession(Session session) {
        return restTemplate.postForObject(userServiceUrl + "/session", session, Session.class);
    }

    public Optional<Session> getSessionByToken(String token) {
        try {
            ResponseEntity<Session> response = restTemplate.getForEntity(userServiceUrl + "/session/" + token, Session.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error retrieving session: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Session updateSession(Session session) {
//        return restTemplate.postForObject(userServiceUrl + "/session/" + session.getToken(), session, Session.class);
        try {
            HttpEntity<Session> requestEntity = new HttpEntity<>(session);
            ResponseEntity<Session> response = restTemplate.exchange(
                    userServiceUrl + "/session/" + session.getToken(),
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
                    userServiceUrl + "/session",
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
}
