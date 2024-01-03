package es.uca.iw.telefonuca.user.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;
import jakarta.annotation.security.PermitAll;

@PageTitle("Inicio")
@PermitAll
@Route(value = "userhome", layout = MainLayout.class)
public class UserHomeView extends VerticalLayout {

    public UserHomeView() {

        add(new H1("Bienvenid@"));
        add(new H2("zona privada de la web"));

    }
}
