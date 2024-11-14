package dev.example.restaurant.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@AnonymousAllowed
@Route("")
@PageTitle("RestaurantVaadin")
public class MainLayout extends AppLayout {

    private final AuthenticationContext authenticationContext;

    public MainLayout(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
        createHeader();
        createDrawer();
        addNavbarContent();
    }

    private void createHeader() {
        H1 logo = new H1("RestaurantVaadin");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

    private void createDrawer() {
        RouterLink homeLink = new RouterLink("Home", HomeView.class);
        RouterLink bookingLink = new RouterLink("Booking", BookingView.class);
        RouterLink customerLink = new RouterLink("Customer", CustomerView.class);
        RouterLink orderLink = new RouterLink("Order", OrderView.class);
        RouterLink loginLink = new RouterLink("Login", LoginView.class);

        customerLink.setHighlightCondition(HighlightConditions.sameLocation());

        // Add icons to each link
        homeLink.addComponentAsFirst(new Icon(VaadinIcon.HOME));
        bookingLink.addComponentAsFirst(new Icon(VaadinIcon.CALENDAR));
        customerLink.addComponentAsFirst(new Icon(VaadinIcon.USER));
        orderLink.addComponentAsFirst(new Icon(VaadinIcon.CART));
        loginLink.addComponentAsFirst(new Icon(VaadinIcon.SIGN_IN));

        // Create a VerticalLayout for the main menu items
        VerticalLayout mainMenu = new VerticalLayout(
                homeLink,
                customerLink,
                bookingLink,
                orderLink
        );

        // Create a VerticalLayout for the entire drawer content
        VerticalLayout drawerContent = new VerticalLayout(mainMenu, loginLink);
        drawerContent.setSizeFull();
        drawerContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        drawerContent.setAlignItems(FlexComponent.Alignment.STRETCH);

        // Add some spacing and styling
        drawerContent.setSpacing(true);
        drawerContent.setPadding(true);
        // Set the login link to expand, pushing it to the bottom
        loginLink.getElement().getStyle().set("margin-top", "auto");


        addToDrawer(drawerContent);
    }

    private void addNavbarContent() {
        var viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE,
                LumoUtility.Flex.GROW);
        // Check if a user is logged in
        if (authenticationContext.isAuthenticated()) {
            var logout = new Button("Logout " + authenticationContext.getPrincipalName().orElse(""),
                    event -> {
                        authenticationContext.logout();
                        getUI().ifPresent(ui -> ui.getPage().setLocation("/home"));
                    });

            var header = new Header(viewTitle, logout);
            header.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX,
                    LumoUtility.Padding.End.MEDIUM, LumoUtility.Width.FULL);

            addToNavbar(false, header);
        }




    }
}