package com.github.redxiiikk.learn.spock.parallel

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.LocalDateTime

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
abstract class AbstractIntegrationTest extends Specification {

    @Autowired
    protected MockMvc mockMvc

    protected def setup() {
        log.info("time track: setup: " + this.class.simpleName + "： start :" + LocalDateTime.now())
    }

    protected def cleanup() {
        log.info("time track: cleanup: " + this.class.simpleName + "： end :" + LocalDateTime.now())
    }
}
