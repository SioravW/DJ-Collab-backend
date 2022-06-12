package nl.fontys.djcollab.Database.Repository;

import nl.fontys.djcollab.Domain.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findByExternalId(UUID externalId);
}
