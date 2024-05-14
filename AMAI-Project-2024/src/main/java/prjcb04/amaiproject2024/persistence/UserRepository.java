package prjcb04.amaiproject2024.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import prjcb04.amaiproject2024.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    public User findByVerificationCode(String code);
}
