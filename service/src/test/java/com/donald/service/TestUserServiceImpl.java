package com.donald.service;


import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.page.PagedResponse;
import com.donald.service.exception.EntityNotFoundException;
import com.donald.service.mapper.UserMapper;
import com.donald.service.model.User;
import com.donald.service.repository.RoleRepository;
import com.donald.service.repository.UserRepository;
import com.donald.service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class TestUserServiceImpl {

    @Mock
    private  UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  RoleRepository roleRepository;
    @Mock
    private  UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private PodamFactory podamFactory;

    @BeforeEach
    void setUpPodam() {
        podamFactory = new PodamFactoryImpl();
    }


    @Test
    @DisplayName("Find user in db, using id and return user dto")
    void getUserById() {
        User user = new User(1L, "donald", "donald", new HashSet<>(), new ArrayList<>());
        UserDto userDto = new UserDto(1L, "donald", new HashSet<>());
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.userToUserDto(user)).thenReturn(userDto);

        UserDto resp = userService.getUserById(1L);
        assertEquals(resp, userDto);
    }

    @Test
    @DisplayName("Get user by id,  throws exception")
    void getUserById_throwsException() {
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(0L);
        });
    }

    @Test
    @DisplayName("Get all users")
    void getAllUsers() {
        int pageNo = 0;
        int pageSize = 2;
        Pageable paging = PageRequest.of(pageNo, pageSize);

        UserDto userDto = new UserDto(1L, "donald", new HashSet<>());
        List<UserDto> listaDto= new ArrayList<>();
        listaDto.add(userDto);
        User user = new User(1L, "donald", "donald", new HashSet<>(), new ArrayList<>());

        List<User> lista= new ArrayList<>();
        lista.add(user);
        Page<User> page = new PageImpl<>(lista);

        Mockito.when(userRepository.findAll(paging)).thenReturn(page);
        Mockito.when(userMapper.usersToUserDtos(lista)).thenReturn(listaDto);

        PagedResponse<UserDto> response = userService.getAllUsers(pageNo, pageSize);

        assertEquals(response.getContent().size(), 1);

    }


}
