package ru.Art3m1y.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsById(long id);
    boolean existsByPerson(Person person);
    void deleteByPerson(Person person);
    Optional<RefreshToken> findByPerson(Person person);
}
