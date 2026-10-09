package com.springmatter.relearnspringboot.mapper;


import com.springmatter.relearnspringboot.dto.record.response.DocumentDto;
import com.springmatter.relearnspringboot.entity.user.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encryptedPayload", ignore = true)
    @Mapping(target = "fileReference", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Document toEntity(DocumentDto dto);


    @Mapping(target = "content", ignore = true)
    @Mapping(target = "attachmentFile", ignore = true)
    DocumentDto toDto(Document entity);
}
