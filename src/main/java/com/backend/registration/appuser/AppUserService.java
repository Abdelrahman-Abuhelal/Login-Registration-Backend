package com.backend.registration.appuser;

import com.backend.registration.registration.token.ConfirmationToken;
import com.backend.registration.registration.token.ConfirmationTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND="User with email %s not found";

    private final BCryptPasswordEncoder passwordEncoder;

    private  final AppUserRepository appUserRepository;

    private final ConfirmationTokenService confirmationTokenService;


    public AppUserService(BCryptPasswordEncoder passwordEncoder, AppUserRepository appUserRepository, ConfirmationTokenService confirmationTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND,email)));
    }

    public String register(AppUser appUser){
        boolean isUserExist=appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if (isUserExist){
            throw new IllegalStateException("Email is Already signed up");
        }
        String encodedPassword =passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
    appUserRepository.save(appUser);

        String generatedToken= UUID.randomUUID().toString();
        ConfirmationToken token=new ConfirmationToken(generatedToken, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),appUser);
        confirmationTokenService.saveConfirmationToken(token);
        return generatedToken;
    }


    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
