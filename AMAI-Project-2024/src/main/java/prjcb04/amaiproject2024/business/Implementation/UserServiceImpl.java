package prjcb04.amaiproject2024.business.Implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import prjcb04.amaiproject2024.business.UserService;
import prjcb04.amaiproject2024.config.security.token.JwtUtil;
import prjcb04.amaiproject2024.domain.Role;
import prjcb04.amaiproject2024.persistence.UserRepository;
import prjcb04.amaiproject2024.domain.User;
import prjcb04.amaiproject2024.business.dto.UserDTO;
import jakarta.transaction.Transactional;
import prjcb04.amaiproject2024.persistence.mapper.UserMapper;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (!StringUtils.hasText(userDTO.getUsername()) || !StringUtils.hasText(userDTO.getEmail()) || !StringUtils.hasText(userDTO.getPassword())) {
            throw new IllegalArgumentException("Username, email, and password must not be empty");
        }
        User user = prepareUserEntity(userDTO);
        user = userRepository.save(user);
        return buildUserDTOwithJwt(user);
    }
    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return Optional.ofNullable(userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id)));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(userDTO.getUsername());
                    existingUser.setEmail(userDTO.getEmail());
                    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty() &&
                            !passwordEncoder.matches(userDTO.getPassword(), existingUser.getPasswordHash())) {
                        existingUser.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
                    }
                    if (userDTO.getRole() != null) {
                        existingUser.setRoles(userDTO.getRole().stream().map(Role::valueOf).toList());
                    } else {
                        existingUser.setRoles(Collections.emptyList());
                    }
                    userRepository.save(existingUser);
                    return mapToDto(existingUser);
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    //mail actions, registration and login

    @Override
    public void register(User user, String siteURL) {
        //String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPasswordHash(user.getPasswordHash());

        String randomCode = stringGeneration();
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        userRepository.save(user);

        sendVerificationEmail(user, siteURL);
    }


    @Override
    public void sendVerificationEmail(User user, String siteURL) {
        String toAddress = user.getEmail();
        String fromAddress = "vrachkapoznavachka@gmail.com";
        String senderName = "AMAI";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "AMAI.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", user.getFullName());
            siteURL =siteURL.replace("register","verify");
            String verifyURL = siteURL + "?code=" + user.getVerificationCode();

            content = content.replace("[[URL]]", verifyURL);
            helper.setText(content, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }

    }

    //private support methods
    private String stringGeneration() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private User prepareUserEntity(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        if (userDTO.getId() == null) {
            user.setRoles(Collections.singletonList(Role.USER));
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        }
        return user;
    }
    private UserDTO buildUserDTOwithJwt(User user) {
        UserDTO dto = userMapper.toDto(user);
        dto.setJwt(jwtUtil.generateToken(dto.getUsername()));
        return dto;
    }

    @Override
    public Optional<UserDTO> login(UserDTO userDTO) {
        return userRepository.findByUsername(userDTO.getUsername())
                .filter(user -> passwordEncoder.matches(userDTO.getPassword(), user.getPasswordHash()))
                .map(this::mapToDto);
    }

    private UserDTO mapToDto(User user) {
        UserDTO dto = userMapper.toDto(user);
        dto.setJwt(jwtUtil.generateToken(user.getUsername()));
        return dto;
    }
}
