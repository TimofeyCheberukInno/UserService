package com.app.impl.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.impl.exception.UserNotFoundException;
import com.app.impl.infrastructure.cache.CardCacheService;
import com.app.impl.dto.cardDtos.CardWithUserDto;
import com.app.impl.entity.User;
import com.app.impl.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final CardCacheService cacheService;
    private static final String CARD_NOT_FOUND_BY_ID_MSG = "Card with id %d was not found";
    private static final String LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG = "Cards not found for ids: ";
    private static final String USER_NOT_FOUND_BY_ID_MSG = "User with id %d not found";

    @Autowired
    public CardServiceImpl(
            CardRepository cardRepository,
            UserRepository userRepository,
            CardMapper cardMapper,
            CardCacheService cacheService
    ) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardMapper = cardMapper;
        this.cacheService = cacheService;
    }

    @Override
    @Transactional
    public CardDto create(CardCreateDto cardCreateDto) {
        User user = userRepository.findById(cardCreateDto.userId()).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MSG, cardCreateDto.userId())));
        Card card = cardMapper.toEntity(cardCreateDto);
        card.setUser(user);
        Card createdCard = cardRepository.save(card);

        CardDto dto = cardMapper.toDto(createdCard);
        cacheService.saveCardWithoutUser(dto);

        return dto;
    }

    @Override
    @Transactional
    public int update(CardUpdateDto cardUpdateDto) {
        cardRepository.findById(cardUpdateDto.id()).orElseThrow(() -> new CardNotFoundException(String.format(CARD_NOT_FOUND_BY_ID_MSG,  cardUpdateDto.id())));
        Card card = cardMapper.toUpdateEntity(cardUpdateDto);

        int cntOfUpdatedCards = cardRepository.updateCard(card);
        cacheService.updateCardWithoutUser(cardMapper.toDto(cardRepository.findById(cardUpdateDto.id()).get()));
        // updates cache of card with user
        findByIdWithUser(cardUpdateDto.id());

        return cntOfUpdatedCards;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException(String.format(CARD_NOT_FOUND_BY_ID_MSG, id)));
        cardRepository.deleteById(id);

        cacheService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto findById(Long id) {
        Optional<CardDto> cachedCard = cacheService.getByIdWithoutUser(id);

        if(cachedCard.isPresent()) {
            return cachedCard.get();
        }
        Card card = cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException(String.format(CARD_NOT_FOUND_BY_ID_MSG, id))
        );

        return cardMapper.toDto(card);
    }

    @Override
    @Transactional(readOnly = true)
    public CardWithUserDto findByIdWithUser(Long id) {
        Optional<CardWithUserDto> cachedCard = cacheService.getByIdWithUser(id);
        if(cachedCard.isPresent()) {
            return cachedCard.get();
        }

        Card card = cardRepository.findByIdWithUser(id).orElseThrow(
                () -> new CardNotFoundException(String.format(CARD_NOT_FOUND_BY_ID_MSG, id))
        );
        CardWithUserDto dto = cardMapper.toDtoWithUser(card);
        cacheService.saveCardWithUser(dto);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDto> findByIds(Collection<Long> ids) {
        List<CardDto> cachedCards = cacheService.getByIdsWithoutUser(ids);
        Set<Long> cachedCardsIds = cachedCards.stream()
                .map(CardDto::id)
                .collect(Collectors.toSet());

        List<Long> notCachedIds = ids.stream()
                .filter(id -> !cachedCardsIds.contains(id))
                .toList();

        List<Card> cards = cardRepository.findAllById(notCachedIds);

        if(cards.size() < notCachedIds.size()) {
            Set<Long> existingIds = cards.stream()
                    .map(Card::getId)
                    .collect(Collectors.toSet());

            List<Long> notExistingIds = notCachedIds.stream()
                    .filter(id -> !existingIds.contains(id))
                    .toList();

            throw new CardNotFoundException(LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG + notExistingIds);
        }

        return cardMapper.toDtoList(cards);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardWithUserDto> findByIdsWithUser(Collection<Long> ids) {
        List<CardWithUserDto> cachedCards = cacheService.getByIdsWithUser(ids);
        Set<Long> cachedCardsIds = cachedCards.stream()
                .map(CardWithUserDto::id)
                .collect(Collectors.toSet());

        List<Long> notCachedIds = ids.stream()
                .filter(id -> !cachedCardsIds.contains(id))
                .toList();

        List<Card> cards = cardRepository.findAllByIdsWithUser(ids);

        if(cards.size() < notCachedIds.size()) {
            Set<Long> existingIds = cards.stream()
                    .map(Card::getId)
                    .collect(Collectors.toSet());

            List<Long> notExistingIds = notCachedIds.stream()
                    .filter(id -> !existingIds.contains(id))
                    .toList();

            throw new CardNotFoundException(LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG + notExistingIds);
        }

        return cardMapper.toDtoWithUserList(cards);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDto> findAll() {
        return cardMapper.toDtoList(cardRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardWithUserDto> findAllWithUser() {
        return cardMapper.toDtoWithUserList(cardRepository.findAllWithUser());
    }
}
