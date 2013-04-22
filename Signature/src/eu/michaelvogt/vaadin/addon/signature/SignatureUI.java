package eu.michaelvogt.vaadin.addon.signature;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureData;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class SignatureUI extends UI {
    private AbstractLayout content;

    private CheckBox readOnly;
    private Signature signature;

    private Image savedSignature;

    private boolean isMobile = false;

    @Override
    protected void init(VaadinRequest request) {
        String location = request.getParameter("v-loc");
        String[] split = location.split("#");
        if (2 > split.length) {
            createFullLayout();
        } else {
            createEntryLayout(split[1]);
        }

        setContent(content);
    }

    private void createEntryLayout(String string) {
        isMobile = true;
        createSignatureWidget("100%", "100%");
    }

    private void createFullLayout() {
        content = new CssLayout();
        content.setSizeFull();
        content.addComponent(new Label("Sample Signature field"));

        readOnly = new CheckBox("Read only");
        readOnly.addValueChangeListener(new EditingListener());
        content.addComponent(readOnly);

        createSignatureWidget();

        savedSignature = new Image();
        content.addComponent(savedSignature);
    }

    private void createSignatureWidget() {
        content = new CssLayout();
        content.setSizeUndefined();
        content.addComponent(new Label("Sample Signature field"));

        signature = new Signature();
        signature.setWidth("100%");
        signature.setHeight("100%");
        signature.setCaption("Signature");
        signature.addSaveListener(new SaveListener());
        signature.allowExternalSigning();
        content.addComponent(signature);
    }

    private void createSignatureWidget(String width, String height) {
        createSignatureWidget();
        content.setWidth(width);
        content.setHeight(height);
    }

    public class SaveListener implements SaveCallback {
        @Override
        public void onSave(SignatureData imageData) {
            if (!isMobile) {
                savedSignature.setSource(new ExternalResource(imageData
                        .getDataUrl(), "image/png"));
            }

            // store imagedata and decide if the signature is allowed to change
            // update shared state
        }
    }

    private class EditingListener implements ValueChangeListener {
        @Override
        public void valueChange(ValueChangeEvent event) {
            signature.setReadOnly(readOnly.getValue());
        }
    }
}