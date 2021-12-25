package com.nankiewic.libraryappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String category;
    private LocalDateTime addDate;
    private String publishYear;
}
