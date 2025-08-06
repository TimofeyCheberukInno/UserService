package com.app.impl.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.impl.dto.userDtos.UserUpdateDto;
import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.service.UserService;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateDto createDto){
        UserDto dto = userService.create(createDto);
        return new ResponseEntity<>(
                dto,
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<Integer> updateUser(@RequestBody @Valid UserUpdateDto updateDto){
        int countOfUpdatedEntities = userService.update(updateDto);
        return new ResponseEntity<>(
                countOfUpdatedEntities,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        UserDto dto = userService.findById(id);
        return new ResponseEntity<>(
                dto,
                HttpStatus.OK
        );
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam @Email String email){
        UserDto dto = userService.findByEmail(email);
        return new ResponseEntity<>(
                dto,
                HttpStatus.OK
        );
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<UserDto>> getListOfUsersByIds(@RequestParam List<Long> ids){
        List<UserDto> dtos = userService.findAllByIds(ids);
        return new ResponseEntity<>(
                dtos,
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(){
        List<UserDto> dtos = userService.findAll();
        return new ResponseEntity<>(
                dtos,
                HttpStatus.OK
        );
    }
}
