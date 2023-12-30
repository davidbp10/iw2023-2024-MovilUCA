package es.uca.iw.telefonuca.user.unit;

import es.uca.iw.telefonuca.user.ObjectMother;
import es.uca.iw.telefonuca.user.domain.User;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ivanruizrube
 */

class UserTest {

    @Test
    void shouldProvideUsername() {

        // Given
        // a certain user (not stored on the database)
        User testUser = ObjectMother.createTestUser("john");

        // When
        // I invoke getUsername method
        String username = testUser.getUsername();

        // Then the result is equals to the provided username
        assertThat(username.equals("john")).isTrue();

    }

}
