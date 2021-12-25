package com.nankiewic.libraryappbackend.service;

import com.nankiewic.libraryappbackend.dto.BookDTO;
import com.nankiewic.libraryappbackend.exception.PermissionDeniedException;
import com.nankiewic.libraryappbackend.model.Book;
import com.nankiewic.libraryappbackend.repository.BookRepository;
import com.nankiewic.libraryappbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public List<BookDTO> getAllBooks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return bookRepository.findAllByUser(auth.getName());
    }

    public BookDTO getBookById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return bookRepository.findByBookIdAndUserEmail(id, auth.getName()).orElseThrow(
                () -> new EntityNotFoundException("resources not found"));
    }

    public Book addBook(BookDTO bookDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Book book = Book.builder()
                .author(bookDTO.getAuthor())
                .title(bookDTO.getTitle())
                .category(bookDTO.getCategory())
                .publishYear(bookDTO.getPublishYear())
                .addDate(LocalDateTime.now())
                .user(userRepository.findByEmail(auth.getName()))
                .build();
        return bookRepository.save(book);
    }

    public Book updateBook(BookDTO bookDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Book book = bookRepository.findById(bookDTO.getId()).orElseThrow(
                () -> new EntityNotFoundException("resources not found"));
        if (book.getUser().getEmail().equals(auth.getName())) {
            book.setAuthor(bookDTO.getAuthor());
            book.setTitle(bookDTO.getTitle());
            book.setCategory(bookDTO.getCategory());
            book.setAddDate(LocalDateTime.now());
            book.setPublishYear(book.getPublishYear());
            return bookRepository.save(book);
        } else throw new PermissionDeniedException("permission denied");
    }

    public void deleteBook(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("resources not found"));
        if (book.getUser().getEmail().equals(auth.getName())) {
            bookRepository.deleteById(id);
        }
    }
}
