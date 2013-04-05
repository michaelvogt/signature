package eu.michaelvogt.vaadin.addon.signature;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class SignatureUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout content = new VerticalLayout();
        setContent(content);

        content.addComponent(new Label("Sample Signature field"));

        Signature signature = new Signature();
        signature.setWidth("300px");
        signature.setHeight("200px");
        signature.setCaption("Signature");
        content.addComponent(signature);
    }
}