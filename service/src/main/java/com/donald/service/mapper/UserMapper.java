package com.donald.service.mapper;
import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.request.UserRegisterRequest;
import com.donald.service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target="roles",
            expression= "java(" +
            "user.getRoles().stream()" +
            ".map(role -> role.getName().name())" +
            ".collect(java.util.stream.Collectors.toSet()))")
    UserDto userToUserDto(User user);

    @Mapping(target="roles", ignore = true)
    User registerRequestToUser(UserRegisterRequest registerRequest);

    List<UserDto> usersToUserDtos(List<User> users);


}
