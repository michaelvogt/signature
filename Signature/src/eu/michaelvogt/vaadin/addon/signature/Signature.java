package eu.michaelvogt.vaadin.addon.signature;

import java.util.UUID;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import eu.michaelvogt.vaadin.addon.signature.SignatureUI.SaveListener;
import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureData;
import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureServerRpc;
import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureState;
import fi.jasoft.qrcode.QRCode;

public class Signature extends CustomField<SignatureData> {
    public static final String SIGNATURE_STYLE = "signature";

    private CssLayout layout;
    private Button editButton;
    private Button externButton;
    private SaveListener saveListener;

    private SignatureServerRpc rpc = new SignatureServerRpcImpl();

    public Signature() {
        buildMainLayout();
        registerRpc(rpc);
    }

    @Override
    protected Component initContent() {
        return layout;
    }

    @Override
    public Class getType() {
        return SignatureData.class;
    }

    @Override
    public SignatureState getState() {
        return (SignatureState) super.getState();
    }

    public void addSaveListener(SaveListener saveListener) {
        this.saveListener = saveListener;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        if (readOnly) {
            getState().isEditing = false;
            layout.removeComponent(editButton);
        } else {
            layout.addComponent(editButton);
        }
    }

    public void allowExternalSigning() {
        externButton = new Button("Sign with Touch");
        externButton.addClickListener(new ExternSignatureListener());
        layout.addComponent(externButton);
    }

    private void buildMainLayout() {
        layout = new CssLayout();

        editButton = new Button("Edit");
        editButton.addClickListener(new EditSignatureListener());
        layout.addComponent(editButton);
    }

    private class EditSignatureListener implements ClickListener {
        @Override
        public void buttonClick(ClickEvent event) {
            layout.removeComponent(editButton);
            getState().isEditing = true;
        }
    }

    private class ExternSignatureListener implements ClickListener {
        @Override
        public void buttonClick(ClickEvent event) {
            // Create specific url for sign ui
            UUID key = UUID.randomUUID();
            String thismobileURL = Page.getCurrent().getLocation().toString()
                    + "#" + key;

            QRCode code = new QRCode();
            code.setValue(thismobileURL);

            // Show code on top of signature field
            Window qr = new Window();
            qr.setContent(code);
            UI.getCurrent().addWindow(qr);

            // When url is accessed, show full screen canvas ui
            // When saved, send to original ui
        }
    }

    private class SignatureServerRpcImpl implements SignatureServerRpc {
        @Override
        public void saveSignature(SignatureData imageData) {
            layout.addComponent(editButton);
            getState().isEditing = false;
            setValue(imageData);

            if (null != saveListener) {
                saveListener.onSave(imageData);
            }
        }

        @Override
        public void cancelSigning() {
            layout.addComponent(editButton);
            getState().isEditing = false;
        }
    }
}
