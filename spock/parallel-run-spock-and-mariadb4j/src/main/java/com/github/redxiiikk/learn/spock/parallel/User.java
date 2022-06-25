package com.github.redxiiikk.learn.spock.parallel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table("t_user")
public class User {
    @Id
    private Integer id;
    private String name;
    private String email;
}
