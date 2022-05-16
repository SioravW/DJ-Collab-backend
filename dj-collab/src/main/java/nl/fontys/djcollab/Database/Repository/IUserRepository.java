package nl.fontys.djcollab.Database.Repository;

import nl.fontys.djcollab.Domain.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{
    Optional<User> findByExternalId(UUID externalId);
}
