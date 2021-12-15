package com.donald.service.service;

import com.donald.service.dto.request.UserRegisterRequest;
import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.page.PagedResponse;

public interface UserService {

    UserDto registerNewUser(UserRegisterRequest registerRequest);

    UserDto getUserById(Long userId);

    UserDto getUserByUsername(String username);

    PagedResponse<UserDto> getAllUsers(Integer pageNo, Integer pageSize);

    UserDto updateUser(Long userId, UserRegisterRequest updateRequest);
}
