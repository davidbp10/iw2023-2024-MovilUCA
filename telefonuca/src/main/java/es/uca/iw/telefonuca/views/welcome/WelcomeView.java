package es.uca.iw.telefonuca.views.welcome;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import es.uca.iw.telefonuca.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.UI;


@PageTitle("Bienvenida")
@Route(value = "welcome", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class WelcomeView extends HorizontalLayout {

    public WelcomeView() {
        // Instancia de Button
        Button loginButton = new Button("Iniciar sesión");
        
        // Agrega un click listener al botón que redirige al formulario de inicio de sesión
        loginButton.addClickListener(e -> {
            UI.getCurrent().navigate("login");
        });
        
        // Añade el botón al layout
        add(loginButton);
        
        // Alinea el botón en el centro
        setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, loginButton);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);
    }
}
