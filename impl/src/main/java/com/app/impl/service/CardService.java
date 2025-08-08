package com.app.impl.service;

import java.util.Collection;
import java.util.List;

import com.app.impl.dto.cardDtos.CardCreateDto;
import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.dto.cardDtos.CardUpdateDto;
import com.app.impl.dto.cardDtos.CardWithUserDto;

public interface CardService {
    CardDto create(CardCreateDto cardCreateDto);
    int update(CardUpdateDto cardUpdateDto);
    void delete(Long id);
    CardDto findById(Long id);
    CardWithUserDto findByIdWithUser(Long id);
    List<CardDto> findByIds(Collection<Long> ids);
    List<CardWithUserDto> findByIdsWithUser(Collection<Long> ids);
    List<CardDto> findAll();
    List<CardWithUserDto> findAllWithUser();
}
