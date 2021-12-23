package com.donald.service;


import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.page.PagedResponse;
import com.donald.service.dto.request.UserRegisterRequest;
import com.donald.service.exception.ConflictException;
import com.donald.service.exception.EntityNotFoundException;
import com.donald.service.mapper.UserMapper;
import com.donald.service.model.User;
import com.donald.service.model.enums.Role;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

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
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(userDto);

        UserDto resp = userService.getUserById(1L);
        assertEquals(resp, userDto);
    }

    @Test
    @DisplayName("Get user by id,  throws exception")
    void getUserById_throwsException() {
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
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

        UserDto userDto = mock(UserDto.class); /*new UserDto(1L, "donald", new HashSet<>());*/
        List<UserDto> listaDto = new ArrayList<>();
        listaDto.add(userDto);
        User user = mock(User.class);  /*new User(1L, "donald", "donald", new HashSet<>(), new ArrayList<>());*/

        List<User> lista= new ArrayList<>();
        lista.add(user);
        Page<User> page = new PageImpl<>(lista);

        when(userRepository.findAll(paging)).thenReturn(page);
        when(userMapper.usersToUserDtos(lista)).thenReturn(listaDto);

        PagedResponse<UserDto> response = userService.getAllUsers(pageNo, pageSize);

        assertEquals(1, response.getContent().size());

    }

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRegisterRequest registerRequest = mock(UserRegisterRequest.class);
        registerRequest.setUsername("A");
        registerRequest.setRoles(new ArrayList<>(List.of("ROLE_ADMIN")));

        User user = mock(User.class);
        user.setUsername("A");
        when(userMapper.registerRequestToUser(registerRequest)).thenReturn(user);

        userService.registerNewUser(registerRequest);

        verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
    }


    @Test
    void shouldThrowWhenRegisteringUserWithTakenUsername() {
        UserRegisterRequest request = podamFactory.manufacturePojo(UserRegisterRequest.class);

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(ConflictException.class, () -> {
            userService.registerNewUser(request);
        });
    }

}
