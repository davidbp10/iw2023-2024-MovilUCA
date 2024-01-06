package es.uca.iw.telefonuca.contract.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.services.ContractManagementService;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

@Route(value = "contractlist", layout = MainLayout.class)
@PermitAll
@PageTitle("Contratos")
public class ContractListView extends VerticalLayout {

    private final ContractManagementService contractService;

    private final Grid<Contract> grid = new Grid<>(Contract.class);

    private TextField filter;

    private RadioButtonGroup<String> filterOptions;

    public ContractListView(ContractManagementService contractService) {
        this.contractService = contractService;

        buildUI();

    }

    private void resetGrid() {
        filter.clear();
        grid.setItems(contractService.loadAllContracts());
    }

    private void buildUI() {
        add(new H1("Contratos"));

        filter = new TextField("Filtrar por: ");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showFilteredData());

        grid.setItems(contractService.loadAllContracts());

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
        }

    }

}
