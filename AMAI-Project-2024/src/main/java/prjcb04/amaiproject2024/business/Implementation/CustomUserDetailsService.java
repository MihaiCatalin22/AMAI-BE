package prjcb04.amaiproject2024.business.Implementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import prjcb04.amaiproject2024.config.security.CustomUserDetails;
import prjcb04.amaiproject2024.persistence.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username)
                .map(user -> new CustomUserDetails(
                        user.getId(),
                        user.getUsername(),
                        user.getPasswordHash(),
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.name()))
                                .toList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }


}