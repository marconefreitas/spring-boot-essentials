package academy.devdojo.springboot.service;

import academy.devdojo.springboot.repository.DevDojoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevDojoUserDetailsService  implements UserDetailsService {

    private final DevDojoUserRepository devDojoUserRepository;

    @Override
    public UserDetails loadUserByUsername(String s) {
        return Optional.ofNullable(devDojoUserRepository.findByUsername(s))
                .orElseThrow(() -> new UsernameNotFoundException("Dev dojo User not found"));
    }
}
