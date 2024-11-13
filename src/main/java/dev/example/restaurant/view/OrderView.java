package dev.example.restaurant.view;


import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "order", layout = MainLayout.class)
public class OrderView extends VerticalLayout {

    public OrderView() {
        add(new H1("Orders"));
        // Add order-related components


    }
}
