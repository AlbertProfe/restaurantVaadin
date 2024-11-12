package dev.example.restaurant.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import dev.example.restaurant.model.Customer;
import dev.example.restaurant.repository.CustomerRepository;

import java.util.List;
import java.util.UUID;

@Route("")
public class CustomerView extends VerticalLayout {

    // connects to the database by a JPA repository ALL crud operations
    private final CustomerRepository customerRepository;
    // binders the form fields to the Customer object
    private final Grid<Customer> grid = new Grid<>(Customer.class);
    private final Binder<Customer> binder = new Binder<>(Customer.class);
    // fields, buttons to the form
    private final TextField name = new TextField("Name");
    private final TextField email = new TextField("Email");
    private final TextField phoneNumber = new TextField("Phone Number");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");


    public CustomerView(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

        // Set up the main view properties
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Create and add the main layout
        add(createMainLayout());

        // Set up data binding
        binder.bindInstanceFields(this);

        // Set up event listeners
        setupEventListeners();

        // Initialize the view
        clearForm();
        refreshGrid();
    }

    // Method to create the main layout
    private Component createMainLayout() {
        // Create the 3-column layout
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);

        // Left column (empty for spacing)
        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.setWidth("20%");

        // Center column (contains all the components)
        VerticalLayout centerColumn = new VerticalLayout();
        centerColumn.setWidth("60%");
        centerColumn.setAlignItems(Alignment.CENTER);

        // Right column (empty for spacing)
        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setWidth("20%");

        // Set up the grid
        grid.setColumns("id", "name", "email", "phoneNumber");
        grid.setSizeFull();

        // Create a form layout
        HorizontalLayout formLayout = new HorizontalLayout(name, email, phoneNumber);
        formLayout.setWidth("100%");
        formLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        // Create a button layout
        HorizontalLayout buttonLayout = new HorizontalLayout(save, delete);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(0.9);
        add(progressBar);

        HorizontalLayout horizontalComponent = createHorizontalComponent();

        // Add components to the center column
        centerColumn.add(
                progressBar,
                new H2("Customer Management"),
                horizontalComponent,
                grid,
                formLayout,
                buttonLayout
        );

        // Add all columns to the main layout
        mainLayout.add(leftColumn, centerColumn, rightColumn);

        return mainLayout;
    }

    private HorizontalLayout createHorizontalComponent() {
        // Create a HorizontalLayout
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        // Create components (assuming these are defined elsewhere or passed as parameters)
        ComboBox<String> comboBox = new ComboBox<>("Type Customer");
        comboBox.setItems(List.of("VIP", "Regular", "New customer", "Ban"));

        DatePicker datePicker = new DatePicker("Start date");

        Accordion accordion = new Accordion();
        Span name = new Span("Sophia Williams");
        Span email = new Span("sophia.williams@company.com");
        Span phone = new Span("(501) 555-9128");

        VerticalLayout personalInformationLayout = new VerticalLayout(name, email, phone);
        personalInformationLayout.setSpacing(false);
        personalInformationLayout.setPadding(false);
        accordion.add("Personal information", personalInformationLayout);

        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("New employee");

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        Button button = new Button("Show dialog", e -> dialog.open());

        add(dialog, button);

        // Add components to the horizontal layout
        horizontalLayout.add(comboBox, datePicker, accordion, button);

        // Configure the layout
        horizontalLayout.setSpacing(true);
        horizontalLayout.setPadding(true);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setWidth("100%");

        // Set individual component alignment (optional)
        horizontalLayout.setVerticalComponentAlignment(Alignment.START, comboBox);
        horizontalLayout.setVerticalComponentAlignment(Alignment.CENTER, datePicker);
        horizontalLayout.setVerticalComponentAlignment(Alignment.END, accordion);

        return horizontalLayout;
    }

    // Method to create the save button
    private Button createSaveButton(Dialog dialog) {
        return  new Button("Save");
    }

    // Method to create the dialog layout
    private VerticalLayout createDialogLayout() {
        return new VerticalLayout();
    }

    // Method to set up event listeners
    private void setupEventListeners() {
        save.addClickListener(e -> saveCustomer());
        delete.addClickListener(e -> deleteCustomer());
        // Add a listener to the grid to handle selection changes
        // and update the form with the selected customer
        // or clear the form if no customer is selected
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                binder.setBean(event.getValue());
            } else {
                clearForm();
            }
        });
    }

    // Methods to save, delete, and clear the form
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

    // Method to delete a customer
    private void deleteCustomer() {
        Customer customer = binder.getBean();
        if (customer != null) {
            customerRepository.delete(customer);
            clearForm();
            refreshGrid();
        }
    }

    // Method to clear the form
    private void clearForm() {
        binder.setBean(new Customer());
    }

    // Method to refresh the grid
    private void refreshGrid() {
        grid.setItems(customerRepository.findAll());
    }
}