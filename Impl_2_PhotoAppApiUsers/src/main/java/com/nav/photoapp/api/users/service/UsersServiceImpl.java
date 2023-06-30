package com.nav.photoapp.api.users.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nav.photoapp.api.users.data.UserEntity;
import com.nav.photoapp.api.users.data.UserRepository;
import com.nav.photoapp.api.users.dto.UserDTO;

@Service
public class UsersServiceImpl implements IUsersService {
	
	UserRepository userRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public UsersServiceImpl(UserRepository userRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;		
	}

	@Override
	public UserDTO createUser(UserDTO userDTO) {

		userDTO.setUserId(UUID.randomUUID().toString());
		userDTO.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
		
//		userEntity.setEncryptedPassword("TE6523DTR90127ST");
		
		userRepository.save(userEntity);
		
		UserDTO returnValue = modelMapper.map(userEntity, UserDTO.class);
		return returnValue;
	}

}
