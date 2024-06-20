package prjcb04.amaiproject2024.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prjcb04.amaiproject2024.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFullName(String fullName);

    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:name%")
    List<User> findByPartialName(@Param("name") String name);
    Optional<User> findByUsername(String username);
    public User findByVerificationCode(String code);
    List<User> findByCalendarSubscribedTrue();
}
