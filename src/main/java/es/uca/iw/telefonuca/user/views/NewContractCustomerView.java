package es.uca.iw.telefonuca.user.views;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;
import jakarta.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

@PermitAll
@PageTitle("Nuevo contrato")
@Route(value = "my-contracts/new", layout = MainLayout.class)
public class NewContractCustomerView extends VerticalLayout {

    private Tabs tabs;

    public NewContractCustomerView() {
        tabs = new Tabs();

        // Primera pestaña
        Tab chooseLinesTab = new Tab("Elegir líneas");
        Div chooseLinesContent = new Div();
        chooseLinesContent.setText("Contenido de la pestaña 'Elegir líneas'");
        // Segunda pestaña
        Tab reviewAndConfirmTab = new Tab("Revisar y confirmar");
        Div reviewAndConfirmContent = new Div();
        reviewAndConfirmContent.setText("Contenido de la pestaña 'Revisar y confirmar'");

        tabs = new Tabs(chooseLinesTab, reviewAndConfirmTab);
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED, TabsVariant.LUMO_EQUAL_WIDTH_TABS);
        add(tabs);

    }
}
