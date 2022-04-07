package sbs.apidemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sbs.apidemo.api.auth.dto.UserDto;
import sbs.apidemo.entity.UserEntity;
import sbs.apidemo.repository.UserRepository;
import sbs.apidemo.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

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

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userEntity.setEmail(userEntity.getEmail().trim());

        if (userRepository.existsByEmail(userEntity.getEmail())) {
            /**
             * TODO 중복 아이디 예외처리 필요
             */
            log.error("이미 가입된 유저입니다.");
            return null;
        }

        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
    public UserDto doLogin(UserDto userDto) {

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userEntity.setEmail(userEntity.getEmail().trim());

        UserEntity findUser = userRepository.findByEmail(userEntity.getEmail())
                .filter(u -> u.getPassword().equals(userEntity.getPassword()))
                .orElse(null);


        if (findUser == null) {
            /**
             * TODO 예외처리 필요
             */
            log.error("아이디 또는 비밀번호가 맞지 않습니다.");
            return null;
        }

        return mapper.map(findUser, UserDto.class);
    }
}
