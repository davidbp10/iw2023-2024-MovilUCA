package es.uca.iw.telefonuca.user.views;

import java.util.UUID;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;
import es.uca.iw.telefonuca.config.TranslationProvider;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;

@Route(value = "userlist", layout = MainLayout.class)
@PermitAll
@PageTitle("Usuarios")
public class UserListView extends VerticalLayout implements LocaleChangeObserver {

    private final UserManagementService userManagementService;
    private final TranslationProvider translationProvider;

    private final Grid<User> grid = new Grid<>(User.class);

    private TextField filter;

    private RadioButtonGroup<String> filterOptions;

    public UserListView(UserManagementService userManagementService, TranslationProvider translationProvider) {
        this.userManagementService = userManagementService;
        this.translationProvider = translationProvider;

        buildUI();
    }

    private void resetGrid() {
        filter.clear();
        grid.setItems(userManagementService.loadAllUsers());
    }

    private void buildUI() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String userListTitle = translationProvider.getTranslation("userList.title", currentLocale);
        add(new H1(userListTitle));

        String filterText = translationProvider.getTranslation("userList.filter", currentLocale);
        filter = new TextField(filterText);
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showFilteredData());

        grid.setItems(userManagementService.loadAllUsers());

        String filterOptionsText = translationProvider.getTranslation("userList.filter", currentLocale);
        filterOptions = new RadioButtonGroup<>();
        filterOptions.setLabel(filterOptionsText);
        filterOptions.setItems(
            translationProvider.getTranslation("userList.filterOptions.all", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.activeUsers", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.alias", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.id", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.email", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.name", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.surname", currentLocale)
        );
        filterOptions.setValue(translationProvider.getTranslation("userList.filterOptions.all", currentLocale)); // Valor por defecto
        filterOptions.addValueChangeListener(event -> resetGrid());

        add(filterOptions, filter, grid);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        Locale currentLocale = event.getLocale();
        ((H1)getComponentAt(0)).setText(translationProvider.getTranslation("userList.title", currentLocale));

        filter.setLabel(translationProvider.getTranslation("userList.filter", currentLocale));

        filterOptions.setLabel(translationProvider.getTranslation("userList.filter", currentLocale));
        filterOptions.setItems(
            translationProvider.getTranslation("userList.filterOptions.all", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.activeUsers", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.alias", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.id", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.email", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.name", currentLocale),
            translationProvider.getTranslation("userList.filterOptions.surname", currentLocale)
        );
    }

    private void showFilteredData() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String selectedOption = filterOptions.getValue();
        if (translationProvider.getTranslation("userList.filterOptions.all", currentLocale).equals(selectedOption)) {
            resetGrid();
        } else if (translationProvider.getTranslation("userList.filterOptions.activeUsers", currentLocale).equals(selectedOption)) {
            grid.setItems(userManagementService.loadActiveUsers());
        } else if (translationProvider.getTranslation("userList.filterOptions.id", currentLocale).equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserById(UUID.fromString(filter.getValue())));
        } else if (translationProvider.getTranslation("userList.filterOptions.email", currentLocale).equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserByEmail(filter.getValue()));
        } else if (translationProvider.getTranslation("userList.filterOptions.name", currentLocale).equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserByName(filter.getValue()));
        } else if (translationProvider.getTranslation("userList.filterOptions.surname", currentLocale).equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserBySurname(filter.getValue()));
        } else if (translationProvider.getTranslation("userList.filterOptions.alias", currentLocale).equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserByUsername(filter.getValue()));
        }
    }

}
