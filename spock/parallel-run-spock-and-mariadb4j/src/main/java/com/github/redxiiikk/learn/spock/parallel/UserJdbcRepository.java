package com.github.redxiiikk.learn.spock.parallel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface UserJdbcRepository extends CrudRepository<User, String> {
}
