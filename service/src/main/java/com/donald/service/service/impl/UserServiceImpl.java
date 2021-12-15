package com.donald.service.service.impl;

import com.donald.service.dto.request.UserRegisterRequest;
import com.donald.service.model.enums.Role;
import com.donald.service.repository.UserRepository;
import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.page.PagedResponse;
import com.donald.service.model.User;
import com.donald.service.exception.BadRequestException;
import com.donald.service.exception.ConflictException;
import com.donald.service.exception.EntityNotFoundException;
import com.donald.service.mapper.UserMapper;
import com.donald.service.repository.RoleRepository;
import com.donald.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.donald.service.model.enums.Role.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private static final Logger logger = LogManager.getLogger("com.donald.tripapplhind.service");


    @Override
    @Transactional
    public UserDto registerNewUser(UserRegisterRequest registerRequest) {
        verifyUsernameUnique(registerRequest.getUsername());

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // no need to check if roles in request are valid, bcs they are validated by the custom annotation
        User userToSave = userMapper.registerRequestToUser(registerRequest);

        setRolesToUserToBeSaved(userToSave, registerRequest);

        User user = userRepository.save(userToSave);

        return userMapper.userToUserDto(user);
    }


    @Override
    public UserDto getUserById(Long userId) {
        User user = getUserWithId(userId);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
                    logger.error(String.format("Could not find user with username: %s", username ));
                    return new EntityNotFoundException("No user found with the provided username");
                }
        );
        return userMapper.userToUserDto(user);
    }

    @Override
    public PagedResponse<UserDto> getAllUsers(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<User> users = userRepository.findAll(paging);
        List<UserDto> usersResponse = userMapper.usersToUserDtos(users.getContent());
        return new PagedResponse<>(usersResponse, users.getSize(), users.getTotalElements());
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserRegisterRequest updateRequest) {
        User oldUser = getUserWithId(userId);
        if (!updateRequest.getUsername().equals(oldUser.getUsername()))
           verifyUsernameUnique(updateRequest.getUsername());

        updateRequest.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

        User userToSave = userMapper.registerRequestToUser(updateRequest);

        setRolesToUserToBeSaved(userToSave, updateRequest);
        verifyAdminNotRemoved(oldUser, userToSave);

        userToSave.setId(oldUser.getId());
        User updatedUser = userRepository.save(userToSave);

        return userMapper.userToUserDto(updatedUser);
    }


    private void setRolesToUserToBeSaved(User userToSave, UserRegisterRequest request){
        userToSave.setRoles(new HashSet<>());
        // roles in the registerRequest will match the name column on the roles table, bcs the request is validated by the custom validator
        request.getRoles().forEach(roleString -> {
            Role[] enumValues = values();
            for (var enumVal : enumValues) {
                if (roleString.equalsIgnoreCase(enumVal.name())) {
                    com.donald.service.model.Role userRole = roleRepository.findByName(enumVal).orElseThrow(
                            () -> new EntityNotFoundException("Role not found!"));
                    userToSave.addRole(userRole);
                }
            }
        });
    }

    private User getUserWithId(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> {
            logger.error(String.format("Could not find user with id: %s", userId ));
            return new EntityNotFoundException("No user found with the provided id");
        });
    }

    private void verifyUsernameUnique(String username) {
        if (username != null && userRepository.existsByUsername(username)) {
            logger.error(String.format("User with username %s, already exists. Can't register/update this user", username));
            throw new ConflictException(String.format("User with username %s, already exists. Provide another request with different username", username));
        }
    }

    private void verifyAdminNotRemoved(User oldUser, User userToSave) {
        com.donald.service.model.Role adminRole = roleRepository.findByName(ROLE_ADMIN).orElseThrow(
                () -> new EntityNotFoundException("Role not found!"));
        Collection<com.donald.service.model.Role> oldRoles = oldUser.getRoles();
        Collection<com.donald.service.model.Role> newRoles = userToSave.getRoles();
        if (oldRoles.contains(adminRole) && !newRoles.contains(adminRole) ) {
                logger.error("Admin can not remove role: ROLE_ADMIN, from users");
                throw new BadRequestException("Admin can not remove role: ROLE_ADMIN, from users");
        }
    }

}
