package com.app.impl.service;

import java.util.Collection;
import java.util.List;

import com.app.impl.dto.cardDtos.CardCreateDto;
import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.dto.cardDtos.CardUpdateDto;
import com.app.impl.dto.cardDtos.CardWithUserDto;

public interface CardService {
    CardDto create(CardCreateDto cardCreateDto);
    CardDto update(CardUpdateDto cardUpdateDto);
    void delete(Long id);
    CardDto findById(Long id);
    CardWithUserDto findByIdWithUser(Long id);
    CardDto findByEmail(String email);
    CardWithUserDto findByEmailWithUser(String email);
    List<CardDto> findAllByIds(Collection<Long> ids);
    List<CardWithUserDto> findAllByIdsWithUser(Collection<Long> ids);
    List<CardDto> findAll();
    List<CardWithUserDto> findAllWithUser();
}
