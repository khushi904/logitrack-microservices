package org.logitrack.authservice.service;

import org.logitrack.authservice.model.Role;
import org.logitrack.authservice.model.User;
import org.logitrack.authservice.model.UserStatus;
import org.logitrack.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrCreateUser(String email) {

        return userRepository.findByEmail(email)
                .map(user -> {
                    user.setEmailVerified(true);
                    user.setUpdatedAt(Instant.now());
                    user.setLastLoginAt(Instant.now());

                    return userRepository.save(user);
                })
                .orElseGet(() -> {

                    User user = new User(
                            email,
                            Role.CUSTOMER,
                            UserStatus.ACTIVE
                    );
                    user.setEmail(email);
                    user.setEmailVerified(true);
                    user.setLastLoginAt(Instant.now());

                    return userRepository.save(user);
                });
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateProfile(String email, String name, String phoneNumber) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (name != null && !name.isBlank()) {
            user.setName(name);
        }

        if (phoneNumber != null && !phoneNumber.isBlank()) {
            user.setPhoneNumber(phoneNumber);
        }

        user.setUpdatedAt(Instant.now());

        return userRepository.save(user);
    }

    public User updateRole(String email, Role role) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        user.setUpdatedAt(Instant.now());

        return userRepository.save(user);
    }

    public User createOrUpdateUser(String email, String name, String phoneNumber) {

        return userRepository.findByEmail(email)
                .map(user -> {
                    if (user.getName() != null
                            && name != null
                            && !user.getName().equals(name)) {
                        throw new IllegalArgumentException(
                                "Name does not match the registered name");
                    }

                    if (user.getPhoneNumber() != null
                            && phoneNumber != null
                            && !user.getPhoneNumber().equals(phoneNumber)) {
                        throw new IllegalArgumentException(
                                "Phone number does not match the registered phone number");
                    }

                    user.setUpdatedAt(Instant.now());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User user = new User(
                            email,
                            Role.CUSTOMER,
                            UserStatus.ACTIVE
                    );

                    user.setName(name);
                    user.setPhoneNumber(phoneNumber);
                    user.setEmailVerified(false);
                    user.setUpdatedAt(Instant.now());

                    return userRepository.save(user);
                });
    }
}