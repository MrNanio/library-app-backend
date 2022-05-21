package com.nankiewic.libraryappbackend.service;

import com.nankiewic.libraryappbackend.dto.BookDTO;
import com.nankiewic.libraryappbackend.dto.BookSimpleDTO;
import com.nankiewic.libraryappbackend.exception.PermissionDeniedException;
import com.nankiewic.libraryappbackend.mapper.BookMapper;
import com.nankiewic.libraryappbackend.model.Book;
import com.nankiewic.libraryappbackend.model.User;
import com.nankiewic.libraryappbackend.model.View.BookBasicView;
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
    private final BookMapper bookMapper;

    public List<BookDTO> getAllBooks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return bookRepository.findAllByUser(auth.getName());
    }

    public BookBasicView getBookById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return bookRepository.findByBookIdAndUserEmail(id, auth.getName()).orElseThrow(
                () -> new EntityNotFoundException("resources not found"));
    }

    public Book addBook(BookSimpleDTO bookSimpleDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        Book book = bookMapper.bookDTOToBook(bookSimpleDTO);
        book.setAddDate(LocalDateTime.now());
        book.setUser(user);
        return bookRepository.save(book);
    }

    public Book updateBook(BookDTO bookDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Book book = bookRepository.findById(bookDTO.getId()).orElseThrow(
                () -> new EntityNotFoundException("resources not found"));

        if (book.getUser().getEmail().equals(auth.getName())) {
            bookMapper.updateVehicleFromRequest(book, bookDTO);
            return bookRepository.save(book);
        } else throw new PermissionDeniedException("permission denied");
    }

    public void deleteBook(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("resources not found"));
        if (book.getUser().getEmail().equals(auth.getName())) {
            bookRepository.deleteById(id);
        }
    }
}
