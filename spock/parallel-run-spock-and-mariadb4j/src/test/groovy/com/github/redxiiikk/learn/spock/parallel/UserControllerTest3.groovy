package com.github.redxiiikk.learn.spock.parallel

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class UserControllerTest3 extends AbstractIntegrationTest {
    static def USER_LIST_TYPE = new TypeReference<List<User>>() {}

    @Autowired
    ObjectMapper objectMapper

    def "test-3"() {
        when: "call api"
        def mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()

        then: "assert"
        def users = objectMapper.readValue(mvcResult.response.contentAsByteArray, USER_LIST_TYPE)
        users != null
        !users.isEmpty()
        users.size() == 6
    }
}
