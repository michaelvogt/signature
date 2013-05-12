package eu.michaelvogt.vaadin.addon.signature.server.sign;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import eu.michaelvogt.vaadin.addon.signature.server.CancelListener;
import eu.michaelvogt.vaadin.addon.signature.server.SaveListener;
import eu.michaelvogt.vaadin.addon.signature.server.Signature;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureData;

public class SignUI extends UI {
    private AbstractLayout content;

    private Signature signature;

    @Override
    protected void init(VaadinRequest request) {
        createLayout();
        setContent(content);
    }

    private void createLayout() {
        content = new CssLayout();
        content.setSizeFull();
        content.addComponent(new Label("Sample Signature field"));

        signature = new Sign();
        signature.setSizeFull();
        signature.setCaption("Please sign here:");
        signature.addSaveListener(new SaveListenerImpl());
        signature.addCancelListener(new CancelListenerImpl());
        content.addComponent(signature);
    }

    private class SaveListenerImpl implements SaveListener {
        @Override
        public void onSave(SignatureData imageData) {
            // Inform the original UI about the new Signature
            System.out.println(imageData.getDataUrl());
        }
    }

    private class CancelListenerImpl implements CancelListener {
        @Override
        public void onCancel() {
            // Inform the original UI that signing was canceled<
        }
    }
}
