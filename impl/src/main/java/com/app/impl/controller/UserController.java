package com.app.impl.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.dto.userDtos.UserUpdateDto;
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
    @ResponseStatus(HttpStatus.CREATED)
	public UserDto createUser(@RequestBody @Valid UserCreateDto createDto) {
		return userService.create(createDto);
	}

	@PutMapping
    @ResponseStatus(HttpStatus.OK)
	public int updateUser(@RequestBody @Valid UserUpdateDto updateDto) {
		return userService.update(updateDto);
	}

	@DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable Long id) {
		userService.delete(id);
	}

	@GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
	public UserDto getUserById(@PathVariable Long id) {
		return userService.findById(id);
	}

	@GetMapping("/by-email")
    @ResponseStatus(HttpStatus.OK)
	public UserDto getUserByEmail(@RequestParam @Email String email) {
		return userService.findByEmail(email);
	}

	@GetMapping("/by-ids")
    @ResponseStatus(HttpStatus.OK)
	public List<UserDto> getListOfUsersByIds(@RequestParam List<Long> ids) {
		return userService.findByIds(ids);
	}

	@GetMapping
    @ResponseStatus(HttpStatus.OK)
	public List<UserDto> getAll() {
		return userService.findAll();
	}
}