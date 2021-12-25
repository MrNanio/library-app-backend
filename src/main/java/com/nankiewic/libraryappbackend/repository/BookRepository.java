package com.nankiewic.libraryappbackend.repository;

import com.nankiewic.libraryappbackend.dto.BookDTO;
import com.nankiewic.libraryappbackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT new com.nankiewic.libraryappbackend.dto.BookDTO(" +
            "b.id, " +
            "b.title, " +
            "b.author, " +
            "b.category, " +
            "b.addDate, " +
            "b.publishYear) FROM Book b WHERE b.user.email=?1")
    List<BookDTO> findAllByUser(String email);

    @Query(value = "SELECT new com.nankiewic.libraryappbackend.dto.BookDTO(" +
            "b.id, " +
            "b.title, " +
            "b.author, " +
            "b.category, " +
            "b.addDate, " +
            "b.publishYear) FROM Book b WHERE b.id=?1 AND b.user.email=?2")
    Optional<BookDTO> findByBookIdAndUserEmail(Long id, String email);
}
