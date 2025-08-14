package fi.nutrifier.services;

import fi.nutrifier.dto.UserDto;
import fi.nutrifier.entities.Role;
import fi.nutrifier.exceptions.EncryptionKeyException;
import fi.nutrifier.exceptions.FailedCryptionException;
import fi.nutrifier.exceptions.FailedDecryptionException;
import fi.nutrifier.exceptions.FailedEncryptionException;
import fi.nutrifier.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import fi.nutrifier.entities.User;
import fi.nutrifier.utils.SecurityUtil;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<User> create(UserDto userDto) {
        try {
            String encryptedEmail = SecurityUtil.encrypt(userDto.getEmail());
            String hashedPassword = SecurityUtil.hashPassword(userDto.getPassword());

            User user = new User(null, encryptedEmail, hashedPassword, Role.ROLE_USER);
            User data = repository.save(user);

            // Plain text email for the return object
            data.setEmail(userDto.getEmail());
            data.setPassword(null);

            return new ResponseEntity<>(data, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<User>> getAll(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<User> data = repository.findAll(pageRequest);

            // Decrypting user data
            Page<User> decryptedUsers = data.map(user -> {
                try {
                    String decryptedEmail = SecurityUtil.decrypt(user.getEmail());
                    user.setEmail(decryptedEmail);
                    user.setPassword(null);
                    return user;
                } catch (FailedCryptionException | EncryptionKeyException e) {
                    return null;
                }
            });

            return new ResponseEntity<>(decryptedUsers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> getById(String id) {
        try {
            User user = repository.findById(id).orElse(null);

            if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            try {
                user.setEmail(SecurityUtil.decrypt(user.getEmail()));
                user.setPassword(null);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } catch (FailedDecryptionException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> update(String id, UserDto userDto) {
        try {
            User existingUser = repository.findById(id).orElse(null);

            if (existingUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            try {
                userDto.setEmail(SecurityUtil.encrypt(userDto.getEmail()));
                User data = repository.save(userDto.toUser());
                data.setPassword(null);
                return new ResponseEntity<>(data, HttpStatus.OK);
            } catch (FailedEncryptionException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> login(String email, String password) {
        try {
            String encryptedEmail = SecurityUtil.encrypt(email);
            User existingUser = repository.findByEmail(encryptedEmail).orElse(null);

            if (existingUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            if (SecurityUtil.checkPassword(password, existingUser.getPassword())) {
                existingUser.setPassword(null);
                existingUser.setEmail(email);
                return new ResponseEntity<>(existingUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> delete(String id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Boolean> isEmailTaken(String email) {
        try {
            String encryptedEmail = SecurityUtil.encrypt(email);
            User found = repository.findByEmail(encryptedEmail).orElse(null);
            if (found != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
