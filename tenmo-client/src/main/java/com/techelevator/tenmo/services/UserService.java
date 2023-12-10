package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserService(String url){
        this.baseUrl = url;
    }

    private  String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getUser(int id){
        User user = null;
        try{
            ResponseEntity<User> response =
                    restTemplate.exchange(baseUrl + id, HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return  user;
    }

    public User addUser(User newUser){
        HttpEntity<User> entity = makeUserEntity(newUser);
        User returnedUser = null;
        try {
            returnedUser = restTemplate.postForObject(baseUrl, entity, User.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedUser;
    }

    public boolean updateUser(User updatedUser){
        HttpEntity<User> entity = makeUserEntity(updatedUser);
        boolean success = false;
        try {
            restTemplate.put(baseUrl + updatedUser.getId(), entity);
            success =true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean deleteUser(int userId){
        boolean success = false;
        try {
            restTemplate.exchange(baseUrl + userId, HttpMethod.DELETE, makeAuthEntity(), Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<User> makeUserEntity(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(user, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


}
