package com.app.impl.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.impl.exception.CardNotFoundException;
import com.app.impl.dto.cardDtos.CardCreateDto;
import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.dto.cardDtos.CardUpdateDto;
import com.app.impl.entity.Card;
import com.app.impl.mapper.CardMapper;
import com.app.impl.repository.CardRepository;
import com.app.impl.service.CardService;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private static final String CARD_NOT_FOUND_BY_ID_MSG = "Card with id %d was not found";
    private static final String CARD_NOT_FOUND_BY_EMAIL_MSG = "Card with email %s was not found";
    private static final String LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG = "Cards not found for ids: ";

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    @Transactional
    public CardDto create(CardCreateDto cardCreateDto) {
        Card card = cardMapper.toEntity(cardCreateDto);
        Card createdCard = cardRepository.save(card);
        return cardMapper.toDto(createdCard);
    }

    @Override
    @Transactional
    public CardDto update(CardUpdateDto cardUpdateDto) {
        Card card = cardMapper.toUpdateEntity(cardUpdateDto);
        Card updatedCard = cardRepository.updateCard(card).orElseThrow(
                () -> new CardNotFoundException(String.format(CARD_NOT_FOUND_BY_ID_MSG,  card.getId()))
        );
        return cardMapper.toDto(updatedCard);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto findById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException(String.format(CARD_NOT_FOUND_BY_ID_MSG, id))
        );
        return cardMapper.toDto(card);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto findByIdWithUser(Long id) {
        Card card = cardRepository.findByIdWithUser(id).orElseThrow(
                () -> new CardNotFoundException(String.format(CARD_NOT_FOUND_BY_ID_MSG, id))
        );
        return cardMapper.toDto(card);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto findByEmail(String email) {
        Card card = cardRepository.findByEmail(email).orElseThrow(
                () -> new  CardNotFoundException(String.format(CARD_NOT_FOUND_BY_EMAIL_MSG, email))
        );
        return cardMapper.toDto(card);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto findByEmailWithUser(String email) {
        Card card = cardRepository.findByEmailWithUser(email).orElseThrow(
                () -> new  CardNotFoundException(String.format(CARD_NOT_FOUND_BY_EMAIL_MSG, email))
        );
        return cardMapper.toDto(card);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDto> findAllByIds(Collection<Long> ids) {
        List<Card> cards = cardRepository.findAllById(ids);

        if(cards.size() < ids.size()) {
            Set<Long> existingIds = cards.stream()
                    .map(Card::getId)
                    .collect(Collectors.toSet());

            List<Long> notExistingIds = ids.stream()
                    .filter(id -> !existingIds.contains(id))
                    .toList();

            throw new CardNotFoundException(LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG + notExistingIds);
        }

        return cardMapper.toDtoList(cards);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDto> findAllByIdsWithUser(Collection<Long> ids) {
        List<Card> cards = cardRepository.findAllByIdsWithUser(ids);

        if(cards.size() < ids.size()) {
            Set<Long> existingIds = cards.stream()
                    .map(Card::getId)
                    .collect(Collectors.toSet());

            List<Long> notExistingIds = ids.stream()
                    .filter(id -> !existingIds.contains(id))
                    .toList();

            throw new CardNotFoundException(LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG + notExistingIds);
        }

        return cardMapper.toDtoList(cards);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDto> findAll() {
        return cardMapper.toDtoList(cardRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDto> findAllWithUser() {
        return cardMapper.toDtoList(cardRepository.findAllWithUser());
    }
}
