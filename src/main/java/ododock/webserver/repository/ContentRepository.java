package ododock.webserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ododock.webserver.domain.content.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
//    Optional<Content> findByName(String name);

//    Optional<Content> findByGenre(String genre);

//    public Content findByAuthor(String author);
//
//    public Content findByDirector(String director);

}
