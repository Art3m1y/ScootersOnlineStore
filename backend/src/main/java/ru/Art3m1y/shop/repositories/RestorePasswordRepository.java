package ru.Art3m1y.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.RestorePassword;

import java.util.Optional;

@Repository
public interface RestorePasswordRepository extends JpaRepository<RestorePassword, Long> {
    Optional<RestorePassword> findByToken(String restoreToken);

    void deleteByPerson(Person person);

    boolean existsByPerson(Person person);
}
