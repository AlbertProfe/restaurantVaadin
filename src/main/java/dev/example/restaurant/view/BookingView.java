package dev.example.restaurant.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.example.restaurant.security.Roles;
import jakarta.annotation.security.RolesAllowed;


//@Secured("ROLE_ADMIN")
@RolesAllowed(Roles.ADMIN)
@Route(value = "booking", layout = MainLayout.class)
@PageTitle("Booking | RestaurantVaadin")
public class BookingView extends VerticalLayout {

    public BookingView() {
        add(new H1("Bookings"));
        // Add conference-related components here
    }
}