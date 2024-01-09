package es.uca.iw.telefonuca.user.unit;

import es.uca.iw.telefonuca.config.TranslationProvider;
import es.uca.iw.telefonuca.user.ObjectMother;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;
import es.uca.iw.telefonuca.user.views.UserActivationView;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.i18n.LocaleContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserActivationViewTest {

    @Autowired
    private UserActivationView userView;

    @Autowired
    private TranslationProvider translationProvider;

    @MockBean
    private UserManagementService userManagementService;

    private String getTranslatedStatus(String key) {
        return translationProvider.getTranslation(key, LocaleContextHolder.getLocale());
    }

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
        String expectedMessage = getTranslatedStatus("userActivation.failure");
        assertThat(userView.getStatus()).isEqualTo(expectedMessage);
    }

    @Test
    void shouldShowSuccessMessageWhenUserIsActivated() {

        // Given
        // a certain user
        User testUser = ObjectMother.createTestUser();

        // and the service is stubbed for the activateUser method
        given(userManagementService.activateUser(anyString(), anyString())).willReturn(true);

        // When
        // Set form values
        userView.setEmail(testUser.getEmail());
        userView.setSecretCode(testUser.getRegisterCode());

        // and invoking the method onActivateButtonClick
        userView.onActivateButtonClick();

        // Then
        verify(userManagementService, times(1)).activateUser(anyString(), anyString());
        // and
        String expectedMessage = getTranslatedStatus("userActivation.success");
        assertThat(userView.getStatus()).isEqualTo(expectedMessage);
    }

}
