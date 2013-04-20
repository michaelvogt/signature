package eu.michaelvogt.vaadin.addon.signature;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class SignatureUI extends UI {

    private Image savedSignature;

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout content = new VerticalLayout();
        setContent(content);

        content.addComponent(new Label("Sample Signature field"));

        Signature signature = new Signature();
        signature.setWidth("300px");
        signature.setHeight("200px");
        signature.setCaption("Signature");
        signature.addSaveListener(new SaveListener());
        content.addComponent(signature);

        savedSignature = new Image();
        content.addComponent(savedSignature);
    }

    public class SaveListener implements SaveCallback {
        @Override
        public void onSave(String imageData) {
            savedSignature.setSource(new ExternalResource(imageData,
                    "image/png"));

            // store imagedata and decide if the signature is allowed to change
            // update shared state
        }
    }
}