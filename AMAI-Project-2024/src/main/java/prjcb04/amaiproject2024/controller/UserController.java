package prjcb04.amaiproject2024.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prjcb04.amaiproject2024.business.UserService;
import prjcb04.amaiproject2024.business.dto.RegisterRequest;
import prjcb04.amaiproject2024.business.dto.SubscribeToCalendarRequest;
import prjcb04.amaiproject2024.business.dto.UserDTO;
import prjcb04.amaiproject2024.domain.User;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);


    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}/calendar/{isSubscribed}")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<Void> invitationsSubscription(@PathVariable Long id,@PathVariable Boolean isSubscribed) {
        SubscribeToCalendarRequest request = SubscribeToCalendarRequest.builder().userId(id).subscribes(isSubscribed).build();
        try {
            userService.subToCalendarToggle(request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == principal.id or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO)
                .map(dto -> ResponseEntity.ok().header("Authorization", "Bearer " + dto.getJwt()).body(dto))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // login, register and email verification
    @GetMapping("/verify/{code}")
    public String verifyUser(@PathVariable String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
    @PostMapping("/register")
    public String processRegister(@RequestBody Map<String, Object> request) {
        ObjectMapper mapper = new ObjectMapper();
        UserDTO userDTO = mapper.convertValue(request.get("user"), UserDTO.class);
        String siteURL = (String) request.get("siteURL");
        userService.register(userDTO, siteURL);
        return "register_success";
    }
//    private String getSiteURL(HttpServletRequest request) {
//        String siteURL = request.getRequestURL().toString();
//        return siteURL.replace(request.getServletPath(), "");
//    }

    //----------------------------------------------------------
}
