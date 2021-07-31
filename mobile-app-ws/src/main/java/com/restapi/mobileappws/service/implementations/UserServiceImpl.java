package com.restapi.mobileappws.service.implementations;

import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.ErrorMessages;
import com.restapi.mobileappws.utils.Utility;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.MBeanAttributeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Utility utils;
    private final ModelMapper modelMapper;


    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalStateException("User already exist");
        }


       for(int i=0; i<user.getAddresses().size(); i++){

         AddressDto address = user.getAddresses().get(i);
           address.setUserDetails(user);
           address.setAddressId(utils.generateAddressId(25));
           user.getAddresses().set(i,address);

       }







//        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntity = modelMapper.map(user, UserEntity.class);







        String publicUserId = utils.generateUserId(25);
        userEntity.setUserId(publicUserId);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);



        UserEntity savedUser = userRepository.save(userEntity);

//        UserDto returnedUser = new UserDto();
//
//        BeanUtils.copyProperties(savedUser,returnedUser);

        UserDto returnedUser = modelMapper.map(savedUser, UserDto.class);



        return returnedUser;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity user = userRepository.findByEmail(email).
                orElseThrow(
                        ()-> new UsernameNotFoundException(email)
                );
//        UserDto returnValue = new UserDto();

//        BeanUtils.copyProperties(user, returnValue);


        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserById(String id) {

        UserEntity user = userRepository.findByUserId(id)
                .orElseThrow(
                        ()-> new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage())
                );


        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {

        UserEntity foundUser = userRepository.findByUserId(userId).
                orElseThrow(
                        () -> new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
                );

        foundUser.setFirstName(userDto.getFirstName());
        foundUser.setLastName(userDto.getLastName());

        UserEntity savedUser = userRepository.save(foundUser);
        UserDto returnedUseDto = new UserDto();

        returnedUseDto.setUserId(savedUser.getUserId());
        returnedUseDto.setEmail(savedUser.getEmail());
        returnedUseDto.setFirstName(savedUser.getFirstName());
        returnedUseDto.setLastName(savedUser.getLastName());




        return returnedUseDto;
    }


    @Transactional
    @Override
    public void deleteUser(String id) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(id);
        if(userEntityOptional.isEmpty()){
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        userRepository.deleteUserEntityByUserId(id);
    }

    @Override
    public List<UserDto> getListOfUsers(int page, int limit) {
            List<UserDto> returnValue = new ArrayList<>();

            if(page>0) page = page -1;

        Pageable pageable = PageRequest.of(page, limit);

        Page<UserEntity> userEntityPage = userRepository.findAll(pageable);
        List<UserEntity> users = userEntityPage.getContent();

        users.forEach((userEntity -> {
//            UserDto userDto = new UserDto();
//            BeanUtils.copyProperties(userEntity, userDto);
            UserDto userDto = modelMapper.map(userEntity, UserDto.class);
            returnValue.add(userDto);
        }));


        return returnValue;
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(
                        ()-> new UsernameNotFoundException(ErrorMessages.INCORRECT_EMAIL_OR_PASSWORD.getErrorMessage())
                );

        return new User(user.getEmail(), user.getEncryptedPassword(), user.getEmailVerificationStatus(), true,
        true, true, new ArrayList<>());

        //return  new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
    }



    @Override
    public Boolean verifyEmailToken(String token) {

        boolean returnValue = false;

        UserEntity user = userRepository.findUserEntityByEmailVerificationToken(token)
                .orElseThrow(
                        () -> new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
                );

              Boolean hasTokenExpired  = Utility.hasTokenExpired(token);

              if(!hasTokenExpired){
                  user.setEmailVerificationToken(null);
                  user.setEmailVerificationStatus(true);
                  userRepository.save(user);
                  returnValue = true;
              }

        return returnValue;
    }


}
