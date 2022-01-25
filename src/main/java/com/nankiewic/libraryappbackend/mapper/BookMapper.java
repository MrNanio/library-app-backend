package com.nankiewic.libraryappbackend.mapper;

import com.nankiewic.libraryappbackend.dto.BookDTO;
import com.nankiewic.libraryappbackend.dto.BookSimpleDTO;
import com.nankiewic.libraryappbackend.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    public abstract Book bookDTOToBook(BookSimpleDTO bookDTO);

    public abstract void updateVehicleFromRequest(@MappingTarget Book book, BookDTO bookDTO);
}



