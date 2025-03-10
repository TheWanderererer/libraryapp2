package ru.springcourse.libraryApp2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.springcourse.libraryApp2.model.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findByDescriptionContainingIgnoreCase(String phrase, Pageable pageable);


    Optional<Book> findByTitle(String title);

    @Modifying
    @Query("UPDATE Book b SET b.readAlready = true WHERE b.id = :id")
    void markAsRead(@Param("id") int id);

}
