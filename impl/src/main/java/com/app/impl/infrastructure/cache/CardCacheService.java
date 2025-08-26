package com.app.impl.infrastructure.cache;

import static com.app.impl.infrastructure.cache.support.CardCacheServiceSupport.getAndValidateObjectsFromCache;
import static com.app.impl.infrastructure.cache.support.CardCacheServiceSupport.makeKey;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.time.Duration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.dto.cardDtos.CardWithUserDto;

@Service
@Slf4j
public class CardCacheService {
	private final RedisTemplate<String, Object> redisTemplate;
	private static final int TIME_TO_LIVE_FOR_CARD_IN_MINUTES = 60;
	private static final String CARDS_CACHE_NAME = "cards";
	private static final String CARDS_WITH_USER_CACHE_NAME = "cards-with-user";

	@Autowired
	public CardCacheService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void saveCardWithoutUser(CardDto card) {
		redisTemplate.opsForValue().set(makeKey(CARDS_CACHE_NAME, card.id()), card,
				Duration.ofMinutes(TIME_TO_LIVE_FOR_CARD_IN_MINUTES));
		log.info("Putting cache for {} with id {}!", CARDS_CACHE_NAME, card.id());
	}

	public void saveCardWithUser(CardWithUserDto card) {
		redisTemplate.opsForValue().set(makeKey(CARDS_WITH_USER_CACHE_NAME, card.id()), card,
				Duration.ofMinutes(TIME_TO_LIVE_FOR_CARD_IN_MINUTES));
		log.info("Putting cache for {} with id {}!", CARDS_WITH_USER_CACHE_NAME, card.id());
	}

	public void updateCardWithoutUser(CardDto card) {
		saveCardWithoutUser(card);
		log.info("Updating cache for {} with id {}...", CARDS_CACHE_NAME, card.id());
	}

	public void updateCardWithUser(CardWithUserDto card) {
		saveCardWithUser(card);
		log.info("Updating cache for {} with id {}...", CARDS_WITH_USER_CACHE_NAME, card.id());
	}

	public Optional<CardWithUserDto> getByIdWithUser(Long id) {
		Object object = redisTemplate.opsForValue().get(makeKey(CARDS_WITH_USER_CACHE_NAME, id));
		if (object instanceof CardWithUserDto) {
			log.info("Cache hit for {} with id {}!", CARDS_WITH_USER_CACHE_NAME, id);
			return Optional.of((CardWithUserDto) object);
		}
		log.info("Cache miss for {} with id {}!", CARDS_WITH_USER_CACHE_NAME, id);

		return Optional.empty();
	}

	public List<CardDto> getByIdsWithoutUser(Collection<Long> ids) {
		List<CardDto> cachedCards = getAndValidateObjectsFromCache(redisTemplate, CARDS_CACHE_NAME, ids, CardDto.class);

		List<Long> cachedCardsIds = cachedCards.stream()
                .map(CardDto::id)
                .toList();
		log.info("Cache hit for {} with ids {}!", CARDS_CACHE_NAME, cachedCardsIds);

		return cachedCards;
	}

	public List<CardWithUserDto> getByIdsWithUser(Collection<Long> ids) {
		List<CardWithUserDto> cachedCards =
                getAndValidateObjectsFromCache(redisTemplate, CARDS_WITH_USER_CACHE_NAME, ids, CardWithUserDto.class);

		List<Long> cachedCardsIds = cachedCards.stream()
                .map(CardWithUserDto::id)
                .toList();
		log.info("Cache hit for {} with ids {}!", CARDS_CACHE_NAME, cachedCardsIds);

		return cachedCards;
	}

	public void delete(Long id) {
		redisTemplate.delete(makeKey(CARDS_CACHE_NAME, id));
		log.info("Evicting cache for {} with id {}!", CARDS_CACHE_NAME, id);
		redisTemplate.delete(makeKey(CARDS_WITH_USER_CACHE_NAME, id));
		log.info("Evicting cache for {} with id {}!", CARDS_WITH_USER_CACHE_NAME, id);
	}
}
