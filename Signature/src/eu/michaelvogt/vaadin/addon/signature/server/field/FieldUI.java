package eu.michaelvogt.vaadin.addon.signature.server.field;

import com.vaadin.annotations.Push;
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

import eu.michaelvogt.vaadin.addon.signature.server.SaveListener;
import eu.michaelvogt.vaadin.addon.signature.server.Signature;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureData;

/**
 * Main UI class
 */
@Push
public class FieldUI extends UI {
    private AbstractLayout content;

    private CheckBox readOnly;
    private CheckBox externalSigning;

    private Signature signature;
    private Image savedSignature;

    @Override
    protected void init(VaadinRequest request) {
        createLayout();
        setContent(content);
    }

    private void createLayout() {
        content = new CssLayout();
        content.setSizeUndefined();
        content.addComponent(new Label("Sample Signature field"));

        readOnly = new CheckBox("Read only");
        readOnly.addValueChangeListener(new EditingListener());
        content.addComponent(readOnly);

        externalSigning = new CheckBox("External Signing");
        externalSigning.addValueChangeListener(new ExternalSigningListener());
        content.addComponent(externalSigning);

        createSignatureWidget();

        savedSignature = new Image();
        content.addComponent(savedSignature);
    }

    private void createSignatureWidget() {
        signature = new Field();
        signature.setSizeFull();
        signature.setCaption("Please sign here:");
        signature.addSaveListener(new SaveListenerImpl());
        content.addComponent(signature);
    }

    private class SaveListenerImpl implements SaveListener {
        @Override
        public void onSave(SignatureData imageData) {
            savedSignature.setSource(new ExternalResource(imageData
                    .getDataUrl(), "image/png"));

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

    private class ExternalSigningListener implements ValueChangeListener {
        @Override
        public void valueChange(ValueChangeEvent event) {
            // FIXME: Ugly
            ((Field) signature)
                    .allowExternalSigning(externalSigning.getValue());
        }
    }
}