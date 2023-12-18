package com.wl.counter.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.wl.counter.client.GitHubClient;
import com.wl.counter.datamodel.GitHubUser;
import com.wl.counter.datamodel.dto.User;
import com.wl.counter.datamodel.dto.UserResponse;
import com.wl.counter.exception.UserNotFoundException;
import com.wl.counter.repository.UserRepository;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    private static final String EXISTING_LOGIN = "existingjohn";
    private static final String NON_EXISTING_LOGIN = "nonexistinguser";

    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @Description("Should correctly find existing user and retrieve his data")
    public void testGetExistedUserData() {
        GitHubUser mockUser = getMockUser();
        when(gitHubClient.getUserData(EXISTING_LOGIN)).thenReturn(mockUser);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(new User(EXISTING_LOGIN, 1L)));

        UserResponse response = userService.getUserByLogin(EXISTING_LOGIN);

        assertNotNull(response);
        assertEquals(EXISTING_LOGIN, response.getLogin());
    }

    @Test
    @Description("Should throw exception if cannot find requested user")
    public void testUserNotFoundCase() {
        when(gitHubClient.getUserData(NON_EXISTING_LOGIN)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByLogin(NON_EXISTING_LOGIN));

        verify(gitHubClient).getUserData(NON_EXISTING_LOGIN);
        verify(userRepository, never()).findByLogin(anyString());
    }

    private GitHubUser getMockUser() {
        GitHubUser mockUser = new GitHubUser();
        mockUser.setId(1L);
        mockUser.setLogin(EXISTING_LOGIN);
        mockUser.setName("John");
        mockUser.setType("User");
        mockUser.setAvatarUrl("https://avatars.githubusercontent.com/u/46875052?v=4");
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setFollowers(4);
        mockUser.setPublicRepos(2);

        return mockUser;
    }
}

