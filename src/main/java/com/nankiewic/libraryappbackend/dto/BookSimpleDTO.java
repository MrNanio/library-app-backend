package com.nankiewic.libraryappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class BookSimpleDTO {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String category;
    @NotBlank
    private String publishYear;
}
