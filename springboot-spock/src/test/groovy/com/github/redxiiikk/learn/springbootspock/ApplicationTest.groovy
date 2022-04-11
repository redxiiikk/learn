package com.github.redxiiikk.learn.springbootspock

import com.github.redxiiikk.learn.springbootspock.mapper.UserMapper
import com.github.redxiiikk.learn.springbootspock.service.UserService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
class ApplicationTest extends AbstractIntegrationTest {
    @Autowired
    private UserService userService

    @SpringBean
    private UserMapper userMapper = Mock()

    @Sql("/user-init.sql")
    def "given user list in db when call names() then get all user name"() {
        given: "mock"
        userMapper.names() >> ['test', 'test-1']

        when: "call name()"
        def names = userService.names()

        then:
        names != null
        names.size() == 2
        names.sort() == ['tom', 'jie'].sort()
    }
}
