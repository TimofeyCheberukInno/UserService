package com.app.impl.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

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

import com.app.impl.dto.cardDtos.CardCreateDto;
import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.dto.cardDtos.CardUpdateDto;
import com.app.impl.dto.cardDtos.CardWithUserDto;
import com.app.impl.service.CardService;

@RestController
@RequestMapping("/api/cards")
@Validated
public class CardController {
	private final CardService cardService;

	@Autowired
	public CardController(CardService cardService) {
		this.cardService = cardService;
	}

	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
	public CardDto createCard(@RequestBody @Valid CardCreateDto createDto) {
		return cardService.create(createDto);
	}

	@PutMapping
    @ResponseStatus(HttpStatus.OK)
	public int updateCard(@RequestBody @Valid CardUpdateDto updateDto) {
		return cardService.update(updateDto);
	}

	@DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCard(@PathVariable @Positive Long id) {
		cardService.delete(id);
	}

	@GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
	public CardWithUserDto getCardWithUserById(@PathVariable @Positive Long id) {
		return cardService.findByIdWithUser(id);
	}

	@GetMapping("/by-ids")
    @ResponseStatus(HttpStatus.OK)
	public List<CardDto> getListOfCardsById(@RequestParam @Valid List<@Positive Long> ids) {
		return cardService.findByIds(ids);
	}

	@GetMapping("/by-ids/with-user")
    @ResponseStatus(HttpStatus.OK)
	public List<CardWithUserDto> getListOfCardsWithUserById(@RequestParam @Valid List<@Positive Long> ids) {
		return cardService.findByIdsWithUser(ids);
	}

	@GetMapping
    @ResponseStatus(HttpStatus.OK)
	public List<CardDto> getAll() {
		return cardService.findAll();
	}

	@GetMapping("/with-user")
    @ResponseStatus(HttpStatus.OK)
	public List<CardWithUserDto> getAllWithUser() {
		return cardService.findAllWithUser();
	}
}