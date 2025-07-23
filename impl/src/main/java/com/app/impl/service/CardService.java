package com.app.impl.service;

import java.util.Collection;
import java.util.List;

import com.app.impl.dto.cardDtos.CardCreateDto;
import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.dto.cardDtos.CardUpdateDto;

public interface CardService {
    CardDto create(CardCreateDto cardCreateDto);
    CardDto update(CardUpdateDto cardUpdateDto);
    void delete(Long id);
    CardDto findById(Long id);
    CardDto findByIdWithUser(Long id);
    CardDto findByEmail(String email);
    CardDto findByEmailWithUser(String email);
    List<CardDto> findAllByIds(Collection<Long> ids);
    List<CardDto> findAllByIdsWithUser(Collection<Long> ids);
    List<CardDto> findAll();
    List<CardDto> findAllWithUser();
}
