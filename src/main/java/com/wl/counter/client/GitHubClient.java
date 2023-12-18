package com.wl.counter.client;

import com.wl.counter.datamodel.GitHubUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public GitHubUser getUserData(String login) {
        return restTemplate.getForObject("https://api.github.com/users/" + login, GitHubUser.class);
    }
}
