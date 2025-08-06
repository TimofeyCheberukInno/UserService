package com.app.impl.mapper;

import java.util.List;

import com.app.impl.dto.cardDtos.CardWithUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.entity.Card;
import com.app.impl.dto.cardDtos.CardCreateDto;
import com.app.impl.dto.cardDtos.CardUpdateDto;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "userId", source = "user.id")
    CardDto toDto(Card card);

    @Mapping(target = "userDto", source = "user")
    @Mapping(target = "userId", source = "user.id")
    CardWithUserDto toDtoWithUser(Card card);

    @Mapping(target = "user.id", source = "userId")
    Card toEntity(CardCreateDto cardDto);

    Card toUpdateEntity(CardUpdateDto cardDto);

    List<CardDto> toDtoList(List<Card> cards);

    List<CardWithUserDto> toDtoWithUserList(List<Card> cards);
}
