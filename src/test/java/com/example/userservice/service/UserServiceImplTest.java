package com.example.userservice.service;

import com.example.userservice.dto.UserRegistDto;
import com.example.userservice.dto.UserVozvratDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.DataFactoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser() {
        UserRegistDto userRegistDto = new UserRegistDto("Lera", "test@test.ru", 28);
        User fackUser = DataFactoryTest.createSampleUser();

        Mockito.when(userRepository.existsByEmail(userRegistDto.getEmail())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(fackUser);

        UserVozvratDto result = userService.createUser(userRegistDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userRegistDto.getEmail(), result.getEmail());

        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void createUser_DuplicateEmail() {
        UserRegistDto userRegistDto = new UserRegistDto("Lera", "test@test.ru", 28);

        Mockito.when(userRepository.existsByEmail(userRegistDto.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(userRegistDto));
        Assertions.assertEquals("Пользователь с таким email уже существует", exception.getMessage());
    }


    @Test
    void findUserById() {
        User fackUser = DataFactoryTest.createSampleUser();

        Mockito.when(userRepository.findById(fackUser.getId())).thenReturn(Optional.of(fackUser));

        UserVozvratDto result = userService.findUserById(fackUser.getId());

        Assertions.assertEquals(fackUser.getId(), result.getId());
        Assertions.assertEquals(fackUser.getEmail(), result.getEmail());

        Mockito.verify(userRepository).findById(fackUser.getId());
    }

    @Test
    void findUserById_NotFound() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertUserNotFoundThrows(() -> userService.findUserById(userId));
    }

    @Test
    void deleteUser() {
        User fackUser = DataFactoryTest.createSampleUser();

        Mockito.when(userRepository.findById(fackUser.getId())).thenReturn(Optional.of(fackUser));

        UserVozvratDto result = userService.deleteUser(fackUser.getId());

        Assertions.assertEquals(fackUser.getId(), result.getId());
        Assertions.assertEquals(fackUser.getEmail(), result.getEmail());

        Mockito.verify(userRepository).delete(fackUser);
    }

    @Test
    void deleteUser_NotFound() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertUserNotFoundThrows(() -> userService.deleteUser(userId));
    }

    @Test
    void updateUser() {
        UserRegistDto userRegistDto = new UserRegistDto("Lera", "test@test.ru", 28);
        User fackUser = DataFactoryTest.createSampleUser();

        Mockito.when(userRepository.findById(fackUser.getId())).thenReturn(Optional.of(fackUser));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(fackUser);

        UserVozvratDto result = userService.updateUser(fackUser.getId(), userRegistDto);

        Assertions.assertEquals(fackUser.getId(), result.getId());
        Assertions.assertEquals(userRegistDto.getName(), result.getName());
        Assertions.assertEquals(fackUser.getEmail(), result.getEmail());
        Assertions.assertEquals(userRegistDto.getAge(), result.getAge());

        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void updateUser_NotFound() {
        Long userId = 1L;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertUserNotFoundThrows(() -> userService.updateUser(userId, new UserRegistDto()));
    }


    @Test
    void getAllUsers() {
        User fackUser = DataFactoryTest.createSampleUser();

        Mockito.when(userRepository.findAll()).thenReturn(List.of(fackUser));

        List<UserVozvratDto> result = userService.getAllUsers();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(fackUser.getId(), result.get(0).getId());

        Mockito.verify(userRepository).findAll();
    }

    private void assertUserNotFoundThrows(Executable action) {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, action);
        Assertions.assertEquals("Пользователь с таким id не существует", exception.getMessage());
    }
}
