package dev.example.restaurant.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@Route(value = "home", layout = MainLayout.class)
@PageTitle("Home | RestaurantVaadin")
public class HomeView extends VerticalLayout {

    public HomeView() {
        add(new H1("Home"));
        add(new Paragraph("Welcome to RestaurantVaadin!"));
        // Add conference-related components here
    }

}