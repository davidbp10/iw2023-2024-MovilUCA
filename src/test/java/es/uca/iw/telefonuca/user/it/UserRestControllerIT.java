package es.uca.iw.telefonuca.user.it;

import es.uca.iw.telefonuca.user.ObjectMother;
import es.uca.iw.telefonuca.user.controllers.UserRestController;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * @author ivanruizrube
 */

@WebMvcTest(controllers = UserRestController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserRestControllerIT {

    @Autowired
    private MockMvc server;

    @MockBean
    private UserManagementService userManagementService;

    @Test
    @WithMockUser
    void shouldReturnListOfUsers() {

        // Given
        // a certain user
        User testUser = ObjectMother.createTestUser();

        // and the service is stubbed for the method loadActiveUsers
        given(userManagementService.loadActiveUsers()).willReturn(List.of(testUser));

        // When
        // Call the HTTP API
        String input = "/api/users";

        // When make a HTTP API Rest invocation and assertion
        try {
            server.perform(get(input).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].username", is(testUser.getUsername())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
