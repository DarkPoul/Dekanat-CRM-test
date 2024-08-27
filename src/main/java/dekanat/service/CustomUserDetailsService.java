package dekanat.service;



import dekanat.entity.UserEntity;
import dekanat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        assert userRepository != null;
        Optional<UserEntity> userEntityOpt = userRepository.findByEmail(email);
        if (userEntityOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        UserEntity userEntity = userEntityOpt.get();
        GrantedAuthority authority = new SimpleGrantedAuthority(userEntity.getRole());
        return new User(userEntity.getEmail(), userEntity.getPassword(), Collections.singleton(authority));
    }

    public String getPIB(String email){
        Optional<UserEntity> userEntityOpt = userRepository.findByEmail(email);
        UserEntity userEntity = userEntityOpt.get();
        return userEntity.getLastname() + " " + userEntity.getFirstname() + " " + userEntity.getPatronymic();
    }


    public String findById(int i) {
        String pib = "";
        if (userRepository.findById((long)i).isPresent()){
            pib = userRepository.findById((long)i).get().getLastname() + " " + userRepository.findById((long)i).get().getFirstname() + " " + userRepository.findById((long)i).get().getPatronymic();
        }
        return pib;
    }
}
