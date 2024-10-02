package dev.example.restaurant.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import dev.example.restaurant.model.Customer;
import dev.example.restaurant.repository.CustomerRepository;
import java.util.UUID;

@Route("")
public class CustomerView extends VerticalLayout {

    private final CustomerRepository customerRepository;
    private final Grid<Customer> grid = new Grid<>(Customer.class);
    private final TextField name = new TextField("Name");
    private final TextField email = new TextField("Email");
    private final TextField phoneNumber = new TextField("Phone Number");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Binder<Customer> binder = new Binder<>(Customer.class);

    public CustomerView(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

        grid.setColumns("id", "name", "email", "phoneNumber");

        HorizontalLayout fields = new HorizontalLayout(name, email, phoneNumber, save, delete);

        add(grid, fields);

        binder.bindInstanceFields(this);

        save.addClickListener(e -> saveCustomer());
        delete.addClickListener(e -> deleteCustomer());

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                binder.setBean(event.getValue());
            } else {
                clearForm();
            }
        });

        clearForm();
        refreshGrid();
    }

    private void saveCustomer() {
        Customer customer = binder.getBean();
        if (customer == null) {
            customer = new Customer();
        }
        if (customer.getId() == null || customer.getId().isEmpty()) {
            customer.setId(UUID.randomUUID().toString());
        }
        binder.writeBeanIfValid(customer);
        customerRepository.save(customer);
        clearForm();
        refreshGrid();
    }

    private void deleteCustomer() {
        Customer customer = binder.getBean();
        if (customer != null) {
            customerRepository.delete(customer);
            clearForm();
            refreshGrid();
        }
    }

    private void clearForm() {
        binder.setBean(new Customer());
    }

    private void refreshGrid() {
        grid.setItems(customerRepository.findAll());
    }
}