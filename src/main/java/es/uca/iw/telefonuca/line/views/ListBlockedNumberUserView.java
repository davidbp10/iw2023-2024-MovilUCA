package es.uca.iw.telefonuca.line.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.line.domain.BlockedNumber;
import es.uca.iw.telefonuca.line.services.BlockedNumberManagementService;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;

import java.util.List;
import java.util.Optional;

import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Mis bloqueos de número")
@Route(value = "/my-blocked-numbers/", layout = MainLayout.class)
public class ListBlockedNumberUserView extends VerticalLayout {

    private final BlockedNumberManagementService blockedNumberManagementService;
    private final AuthenticatedUser authenticatedUser;

    private Grid<BlockedNumber> grid;

    public ListBlockedNumberUserView(BlockedNumberManagementService blockedNumberManagementService,
            AuthenticatedUser authenticatedUser) {
        this.blockedNumberManagementService = blockedNumberManagementService;
        this.authenticatedUser = authenticatedUser;

        buildUI();
    }

    private void buildUI() {
        grid = new Grid<>(BlockedNumber.class);
        grid.setColumns("blocker", "blocked");

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            List<BlockedNumber> blockedNumbers = blockedNumberManagementService.loadBlockedNumberByUserId(user.getId());
            grid.setItems(blockedNumbers);
        }

        add(grid);
    }
}
