package sbs.apidemo.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import sbs.apidemo.base.entity.BaseTimeEntity;

import javax.persistence.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@NoArgsConstructor(access = PROTECTED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    @Setter
    private String email;

    @Column(nullable = false)
    @Setter
    private String name;

    @Column(nullable = false)
    @Setter
    private String password;
}

