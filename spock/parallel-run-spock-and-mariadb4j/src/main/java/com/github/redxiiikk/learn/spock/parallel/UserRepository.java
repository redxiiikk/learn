package com.github.redxiiikk.learn.spock.parallel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserJdbcRepository userJdbcRepository;

    public List<User> queryAll() {
        return StreamSupport.stream(userJdbcRepository.findAll().spliterator(), false).collect(toList());
    }
}
