package prjcb04.amaiproject2024.business.Implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import prjcb04.amaiproject2024.config.security.token.JwtUtil;
import prjcb04.amaiproject2024.domain.Role;
import prjcb04.amaiproject2024.domain.User;
import prjcb04.amaiproject2024.business.dto.UserDTO;
import prjcb04.amaiproject2024.persistence.UserRepository;
import prjcb04.amaiproject2024.persistence.mapper.UserMapper;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");
        user.setRoles(Collections.singletonList(Role.USER));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole(Collections.singletonList("USER"));

        lenient().when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        lenient().when(userMapper.toDto(any(User.class))).thenReturn(userDTO);
        lenient().when(jwtUtil.generateToken(anyString())).thenReturn("dummyToken");
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        lenient().when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    }

    @Test
    void createUser_Successful_ReturnsUserDTOWithJwt() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals("dummyToken", result.getJwt());
        verify(userRepository).save(any(User.class));
    }


    @Test
    void getUserById_UserNotFound_ThrowsEntityNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void updateUser_ExistingUser_ChangesDetected() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userDTO.setPassword("newPassword123");
        userDTO.setRole(Collections.singletonList("ADMIN"));

        UserDTO updatedUserDTO = userService.updateUser(1L, userDTO);

        assertNotNull(updatedUserDTO);
        assertEquals("dummyToken", updatedUserDTO.getJwt());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_ExistingUser_DeletesUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_UserNotFound_ThrowsIllegalArgumentException() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(1L));
    }
}