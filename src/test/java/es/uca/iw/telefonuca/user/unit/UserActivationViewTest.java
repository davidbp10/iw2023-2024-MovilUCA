package es.uca.iw.telefonuca.user.unit;

import es.uca.iw.telefonuca.user.ObjectMother;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;
import es.uca.iw.telefonuca.user.views.UserActivationView;
import es.uca.iw.telefonuca.user.views.UserHomeView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author ivanruizrube
 */

@SpringBootTest
class UserActivationViewTest {

    @Autowired
    private UserActivationView userView;

    @MockBean
    private UserManagementService userManagementService;

    @Test
    void shouldShowFailureMessageWhenUserIsNotActivated() {

        // Given
        // a certain user
        User testUser = ObjectMother.createTestUser();

        // and the service is stubbed for the activateUser method
        given(userManagementService.activateUser(anyString(), anyString())).willReturn(false);

        // When
        // Set form values
        userView.setEmail(testUser.getEmail());
        userView.setSecretCode(testUser.getRegisterCode());

        // and invoking the method onActivateButtonClick
        userView.onActivateButtonClick();

        // Then
        verify(userManagementService, times(1)).activateUser(anyString(), anyString());
        // and
        assertThat(userView.getStatus().equals("Ups. El usuario no ha podido ser activado")).isTrue();
    }

    @Test
    void shouldShowSuccessMessageWhenUserIsActivated() {

        // Given
        // a certain user
        User testUser = ObjectMother.createTestUser();

        // and the service is stubbed for the activateUser method
        given(userManagementService.activateUser(anyString(), anyString())).willReturn(true);
        
        // Restore the initial state of the mock
        reset(userManagementService);

        // When
        // Set form values
        userView.setEmail(testUser.getEmail());
        userView.setSecretCode(testUser.getRegisterCode());

        // and invoking the method onActivateButtonClick
        userView.onActivateButtonClick();

        // Then
        verify(userManagementService, times(2)).activateUser(anyString(), anyString());
        // and
        assertThat(userView.getStatus().equals("Enhorabuena. El usuario ha sido activado")).isTrue();
    }

}
