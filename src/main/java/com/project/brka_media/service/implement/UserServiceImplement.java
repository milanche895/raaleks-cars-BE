package com.project.brka_media.service.implement;

import com.project.brka_media.DTO.UserDTO;
import com.project.brka_media.entity.UserEntity;
import com.project.brka_media.repository.UserRepository;
import com.project.brka_media.security.JwtUtil;
import com.project.brka_media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<UserDTO> getAllUser(){

        return userRepository.findAll()
                .stream()
                .map(userEntity -> new UserDTO(userEntity))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDTO, String id) {

        if(Objects.nonNull(id) && Objects.nonNull(userDTO)) {

            UserEntity userEntity = userRepository.findOneById(id);
            if(Objects.nonNull(userEntity)) {

                userEntity.setName(userDTO.getName());

                userRepository.save(userEntity);

                return userDTO;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please insert id and userDTO model");
        }
    }

    @Override
    public UserDTO getUserById(String id) {

        UserEntity userEntity =  userRepository.findOneById(id);
        System.out.println(userEntity);
        System.out.println(userEntity.getId());
        if (Objects.isNull(userEntity)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        } else {

            UserDTO userDTO = new UserDTO(userEntity);

            return userDTO;
        }

    }
    @Override
    public UserDTO getUserByUsername(String username) {

        UserEntity userEntity =  userRepository.findByUsername(username);
        if (Objects.isNull(userEntity)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        } else {

            UserDTO userDTO = new UserDTO(userEntity);

            return userDTO;
        }

    }
}
