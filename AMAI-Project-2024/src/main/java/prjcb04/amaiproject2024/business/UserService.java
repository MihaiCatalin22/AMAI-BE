package prjcb04.amaiproject2024.business;

import prjcb04.amaiproject2024.business.dto.UserDTO;
import prjcb04.amaiproject2024.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    Optional<UserDTO> getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);

    void register(UserDTO userDTO, String siteURL);
    void sendVerificationEmail(User user, String siteURL);
    boolean verify(String verificationCode);
    Optional<UserDTO> login (UserDTO userDTO);
}
