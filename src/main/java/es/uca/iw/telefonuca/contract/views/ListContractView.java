package es.uca.iw.telefonuca.contract.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.services.ContractManagementService;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "contracts/", layout = MainLayout.class)
@RolesAllowed({"CUSTOMER_SERVICE"})
@PageTitle("Contratos")
public class ListContractView extends VerticalLayout {

    private final ContractManagementService contractService;

    private final Grid<Contract> grid = new Grid<>(Contract.class);

    public ListContractView(ContractManagementService contractService) {
        this.contractService = contractService;

        buildUI();

    }

    private void buildUI() {
        grid.setItems(contractService.loadAll());

        add(grid);

    }

}
