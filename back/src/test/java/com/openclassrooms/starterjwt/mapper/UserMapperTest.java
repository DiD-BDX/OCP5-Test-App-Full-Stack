package com.openclassrooms.starterjwt.mapper;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void shouldMapDtoToEntity() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPassword("password123");

        User user = userMapper.toEntity(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getPassword(), user.getPassword());
    }

    @Test
    void shouldMapEntityToDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("password123");

        UserDto userDto = userMapper.toDto(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getPassword(), userDto.getPassword());       
    }

    @Test
    void shouldMapDtoListToEntityList() {
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("test1@example.com");
        userDto1.setLastName("Doe");
        userDto1.setFirstName("John");
        userDto1.setPassword("password123");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("test2@example.com");
        userDto2.setLastName("Smith");
        userDto2.setFirstName("Jane");
        userDto2.setPassword("password456");

        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        List<User> userList = userMapper.toEntity(userDtoList);

        assertEquals(userDtoList.size(), userList.size());
        for (int i = 0; i < userList.size(); i++) {
            assertEquals(userDtoList.get(i).getId(), userList.get(i).getId());
            assertEquals(userDtoList.get(i).getEmail(), userList.get(i).getEmail());
            assertEquals(userDtoList.get(i).getLastName(), userList.get(i).getLastName());
            assertEquals(userDtoList.get(i).getFirstName(), userList.get(i).getFirstName());
            assertEquals(userDtoList.get(i).getPassword(), userList.get(i).getPassword());
        }
    }

    @Test
    void shouldMapEntityListToDtoList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@example.com");
        user1.setLastName("Doe");
        user1.setFirstName("John");
        user1.setPassword("password123");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test2@example.com");
        user2.setLastName("Smith");
        user2.setFirstName("Jane");
        user2.setPassword("password456");

        List<User> userList = Arrays.asList(user1, user2);

        List<UserDto> userDtoList = userMapper.toDto(userList);

        assertEquals(userList.size(), userDtoList.size());
        for (int i = 0; i < userDtoList.size(); i++) {
            assertEquals(userList.get(i).getId(), userDtoList.get(i).getId());
            assertEquals(userList.get(i).getEmail(), userDtoList.get(i).getEmail());
            assertEquals(userList.get(i).getLastName(), userDtoList.get(i).getLastName());
            assertEquals(userList.get(i).getFirstName(), userDtoList.get(i).getFirstName());
            assertEquals(userList.get(i).getPassword(), userDtoList.get(i).getPassword());
        }
    }
}
