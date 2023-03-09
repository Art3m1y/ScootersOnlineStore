package ru.Art3m1y.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Art3m1y.shop.models.Avatar;
import ru.Art3m1y.shop.models.Person;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByPersonId(long id);
    void deleteByPerson(Person person);
}
