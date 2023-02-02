package com.quest.global.SmartHome.services;

import com.quest.global.SmartHome.dtos.UserViewDTO;
import com.quest.global.SmartHome.exceptions.UserNotFoundException;
import com.quest.global.SmartHome.mapstruct.mappers.MapStructMapper;
import com.quest.global.SmartHome.models.User;
import com.quest.global.SmartHome.repositories.UserRepository;
import com.quest.global.SmartHome.services.impl.IUserService;
import org.bson.types.ObjectId;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapStructMapper mapStructMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MapStructMapper mapStructMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapStructMapper = mapStructMapper;
    }

    @Transactional
    public List<UserViewDTO> listAll() {

       List<User> users
               = userRepository.findAll();

       return mapStructMapper.usersToUsersDTOs(users);
    }

    @Transactional
    public List<User> getAll() {

        return (List<User>) userRepository.findAll();
    }

    @Transactional
    public void createUser(User user) {
        encodePassword(user);
        userRepository.save(user);
    }

    @Transactional
    public UserViewDTO findUserByID(ObjectId id) throws UserNotFoundException {

        try {
            UserViewDTO userViewDTO =
                    mapStructMapper.userToUserDTO(userRepository.findById(id).get());
            return userViewDTO;
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("This User's ID doesn't exist!");
        }

    }

    @Transactional
    public void deleteUserByID(ObjectId id) throws UserNotFoundException {

        User user
                = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw  new UserNotFoundException("Could not find that user !");
        }
        userRepository.delete(user);
    }

    private void encodePassword(User user) {

        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
    }
}