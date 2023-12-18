package com.wl.counter.service;

import com.wl.counter.client.GitHubClient;
import com.wl.counter.datamodel.GitHubUser;
import com.wl.counter.datamodel.dto.User;
import com.wl.counter.datamodel.dto.UserResponse;
import com.wl.counter.exception.UserNotFoundException;
import com.wl.counter.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final GitHubClient gitHubClient;
    private final UserRepository requestRepository;

    public UserService(GitHubClient gitHubClient, UserRepository requestRepository) {
        this.gitHubClient = gitHubClient;
        this.requestRepository = requestRepository;
    }

    public UserResponse getUserByLogin(String login) {

        GitHubUser gitHubUser;

        try {
            gitHubUser = gitHubClient.getUserData(login);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }

        User requestedUser = requestRepository.findByLogin(login)
                .orElseGet(() -> new User(gitHubUser.getLogin(), 0L));

        requestedUser.setRequestCount(requestedUser.getRequestCount() + 1);
        requestRepository.save(requestedUser);

        Double calculations = calculate(gitHubUser);
        return createUserResponseFromGitHubUser(gitHubUser, calculations);
    }

    private Double calculate(GitHubUser user) {
        return 6.0 / user.getFollowers() * (2 + user.getPublicRepos());
    }

    private UserResponse createUserResponseFromGitHubUser(GitHubUser user, double calculations) {
        return UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .type(user.getType())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .calculations(calculations)
                .build();
    }
}
