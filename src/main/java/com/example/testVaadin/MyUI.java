package com.example.testVaadin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    
    private CustomerService service = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid<>(Customer.class);
    private TextField filterText = new TextField();
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField email = new TextField();
private List<Customer> lcustomers ;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout hlayout = new HorizontalLayout();

        filterText.setPlaceholder("filter by name...");
        filterText.addValueChangeListener(e -> updateList());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        firstName.setPlaceholder("name");
        lastName.setPlaceholder("phone");
        email.setPlaceholder("adress");
        
        Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
        Button add= new Button();
        add.setDescription("add");
        
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> filterText.clear());
        add.addClickListener(e -> addToList());

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterText, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        grid.setColumns("firstName", "lastName", "email");

        // add filtering and Grid components to the layout
        hlayout.addComponents(firstName,lastName,email,add);
        layout.addComponents(hlayout,filtering, grid);
  

        // fetch list of Customers from service and assign it to Grid
        updateList();

        setContent(layout);
    }
    public void addToList(){
    	Customer c= new Customer();
    	c.setFirstName(firstName.getValue());
    	c.setLastName(lastName.getValue());
    	c.setEmail(email.getValue());
    	lcustomers	= new ArrayList<>();
    	service.save(c);
    	lcustomers.add(c);
    	 updateList();
    }

    public void updateList() {
        List<Customer> customers = service.findAll(filterText.getValue());
        grid.setItems(customers);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
