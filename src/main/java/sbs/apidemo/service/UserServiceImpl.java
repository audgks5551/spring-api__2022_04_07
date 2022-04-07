package sbs.apidemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sbs.apidemo.apiv1.dto.UserDto;
import sbs.apidemo.entity.UserEntity;
import sbs.apidemo.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    /**
     * 유저 생성
     */
    @Override
    public UserDto doJoin(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            /**
             * TODO 중복 아이디 예외처리 필요
             */
            log.error("이미 가입된 유저입니다.");
            return null;
        }

        UserEntity userEntity = mapper.map(userDto, UserEntity.class); // UserDto -> UserEntity

        /**
         * 중간에서 userEntity의 값을 데이터 변형 또는 임의의 값을 넣고 DB에 저장
         */
        userEntity.setPassword(userEntity.getPassword().trim());

        return mapper.map(userRepository.save(userEntity), UserDto.class); // userEntity -> UserDto
    }
}
