package sbs.apidemo.apiv1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String email;
    private String name;
    private String password;

    private LocalDateTime regDate;
    private LocalDateTime modifiedDate;
}