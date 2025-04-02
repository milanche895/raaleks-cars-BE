package com.project.brka_media.security;

import com.project.brka_media.DTO.UserDTO;
import com.project.brka_media.constants.UserRoleEnum;
import com.project.brka_media.entity.UserEntity;
import com.project.brka_media.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> roles = null;

		UserEntity user = userRepository.findByUsername(username);
		if (user != null) {
			roles = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));
			return new User(user.getUsername(), user.getPassword(), roles);
		}
		throw new UsernameNotFoundException("User not found with the name " + username);
	}
	public UserDTO save(UserDTO userDTO) {
		if (Objects.nonNull(userDTO.getPassword()) && Objects.nonNull(userDTO.getName())) {
			if (Objects.isNull(userRepository.findByUsername(userDTO.getUsername()))) {
				UserEntity newUser = new UserEntity(userDTO);
				newUser.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
				userDTO.setRole(UserRoleEnum.ROLE_CLIENT.toString());
				newUser.setRole(userDTO.getRole());

				userRepository.save(newUser);
				userDTO.setId(newUser.getId());

				return userDTO;
			} else {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already exist");
			}
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please input all data");
		}
	}
}
