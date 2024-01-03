package es.uca.iw.telefonuca.user.views;

import java.util.UUID;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

@Route(value = "userlist", layout = MainLayout.class)
@PermitAll
@PageTitle("Usuarios")
public class UserListView extends VerticalLayout {

    private final UserManagementService userManagementService;

    private final Grid<User> grid = new Grid<>(User.class);

    private TextField filter;

    private RadioButtonGroup<String> filterOptions;

    public UserListView(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;

        buildUI();

    }

    private void resetGrid() {
        filter.clear();
        grid.setItems(userManagementService.loadAllUsers());
    }

    private void buildUI() {
        add(new H1("Usuarios"));

        filter = new TextField("Filtrar por: ");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showFilteredData());

        grid.setItems(userManagementService.loadAllUsers());

        filterOptions = new RadioButtonGroup<>();
        filterOptions.setLabel("Filtrar por:");
        filterOptions.setItems("Todos", "Usuarios activos", "Alias de usuario", "ID del usuario", "Email del usuario",
                "Nombre del usuario", "Apellido del usuario"); // Agrega aquí las opciones que necesites
        filterOptions.setValue("Todos"); // Valor por defecto
        filterOptions.addValueChangeListener(event -> resetGrid());

        add(filterOptions, filter, grid);

    }

    private void showFilteredData() {
        String selectedOption = filterOptions.getValue();
        if ("Todos".equals(filterOptions.getValue())) {
            resetGrid();
        } else if ("Usuarios activos".equals(selectedOption)) {
            grid.setItems(userManagementService.loadActiveUsers());
        } else if ("ID del usuario".equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserById(UUID.fromString(filter.getValue())));
        } else if ("Email del usuario".equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserByEmail(filter.getValue()));
        } else if ("Nombre del usuario".equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserByName(filter.getValue()));
        } else if ("Apellido del usuario".equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserBySurname(filter.getValue()));
        } else if ("Alias de usuario".equals(selectedOption)) {
            grid.setItems(userManagementService.loadUserByUsername(filter.getValue()));
        }
    }

}
