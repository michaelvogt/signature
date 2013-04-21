package eu.michaelvogt.vaadin.addon.signature;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;

import eu.michaelvogt.vaadin.addon.signature.SignatureUI.SaveListener;
import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureData;
import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureServerRpc;
import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureState;

public class Signature extends CustomField<SignatureData> {
    public static final String SIGNATURE_STYLE = "signature";

    private CssLayout layout;
    private Button editButton;
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
