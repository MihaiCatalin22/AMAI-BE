package prjcb04.amaiproject2024.business.Implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import prjcb04.amaiproject2024.domain.User;
import prjcb04.amaiproject2024.persistence.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("pass123");

    }

    @Test
    void createUser_HappyPath() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User createdUser = userService.createUser(new User());
        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_HappyPath() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.getUserById(1L);
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<User> foundUser = userService.getUserById(1L);
        assertFalse(foundUser.isPresent());
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        List<User> users = userService.getAllUsers();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void updateUser_HappyPath() {
        User newUserDetails = new User();
        newUserDetails.setUsername("updatedUser");
        newUserDetails.setEmail("updated@example.com");
        newUserDetails.setPassword("newPass123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(1L, newUserDetails);
        assertNotNull(updatedUser);
        assertEquals(newUserDetails.getUsername(), updatedUser.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, new User()));
    }

    @Test
    void deleteUser_HappyPath() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }


    @Test
    void deleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void verify_HappyPath() {
        String code = "123456";
        user.setVerificationCode(code);
        user.setEnabled(false);

        when(userRepository.findByVerificationCode(code)).thenReturn(user);

        assertTrue(userService.verify(code));
        assertNull(user.getVerificationCode());
        assertTrue(user.isEnabled());
        verify(userRepository).save(user);
    }
    @Test
    void verify_InvalidCode() {
        when(userRepository.findByVerificationCode("invalidCode")).thenReturn(null);
        assertFalse(userService.verify("invalidCode"));
    }

    @Test
    void verify_AlreadyVerified() {
        String code = "123456";
        user.setVerificationCode(code);
        user.setEnabled(true);

        when(userRepository.findByVerificationCode(code)).thenReturn(user);

        assertFalse(userService.verify(code));
    }
}
