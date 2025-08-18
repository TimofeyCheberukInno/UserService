package com.app.impl.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.app.impl.dto.cardDtos.CardCreateDto;
import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.dto.cardDtos.CardUpdateDto;
import com.app.impl.dto.cardDtos.CardWithUserDto;
import com.app.impl.service.CardService;

// FIXME : @ResponseEntity annotation
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
	public ResponseEntity<CardDto> createCard(@RequestBody @Valid CardCreateDto createDto) {
		CardDto dto = cardService.create(createDto);
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Integer> updateCard(@RequestBody @Valid CardUpdateDto updateDto) {
		int countOfUpdatedEntities = cardService.update(updateDto);
		return new ResponseEntity<>(countOfUpdatedEntities, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
		cardService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CardDto> getCardById(@PathVariable Long id) {
		CardDto dto = cardService.findById(id);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@GetMapping("/{id}/with-user")
	public ResponseEntity<CardWithUserDto> getCardWithUserById(@PathVariable Long id) {
		CardWithUserDto dto = cardService.findByIdWithUser(id);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@GetMapping("/by-ids")
	public ResponseEntity<List<CardDto>> getListOfCardsById(@RequestParam List<Long> ids) {
		List<CardDto> dtos = cardService.findByIds(ids);
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@GetMapping("/by-ids/with-user")
	public ResponseEntity<List<CardWithUserDto>> getListOfCardsWithUserById(@RequestParam List<Long> ids) {
		List<CardWithUserDto> dtos = cardService.findByIdsWithUser(ids);
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<CardDto>> getAll() {
		List<CardDto> dtos = cardService.findAll();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@GetMapping("/with-user")
	public ResponseEntity<List<CardWithUserDto>> getAllWithUser() {
		List<CardWithUserDto> dtos = cardService.findAllWithUser();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
}