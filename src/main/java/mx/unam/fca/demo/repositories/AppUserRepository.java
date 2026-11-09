package mx.unam.fca.demo.repositories;

import java.util.Optional;
import mx.unam.fca.demo.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);
}