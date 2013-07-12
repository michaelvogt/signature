package eu.michaelvogt.vaadin.addon.signature.server;

import java.util.UUID;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;

import eu.michaelvogt.vaadin.addon.signature.shared.SignatureData;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureState;

public abstract class Signature extends CustomField<SignatureData> implements
        BroadcastListener {
    public static final String IMAGE_URL = "signatureqrcode.png";

    protected CssLayout layout;

    // TODO: make these lists
    protected SaveListener saveListener;
    protected CancelListener cancelListener;

    protected UUID signatureId;

    public Signature() {
        super();
    }

    @Override
    protected Component initContent() {
        return layout;
    }

    @Override
    public void detach() {
        super.detach();

        // TODO: Needs to be refined.
        // - clear complete signatureId from Broadcaster from Field
        // - unregister sign
        // - inform sign when field was closed
        if (null != signatureId) {
            Broadcaster.unregister(signatureId, this);
            signatureId = null;
        }
    }

    @Override
    public Class<? extends SignatureData> getType() {
        return SignatureData.class;
    }

    @Override
    public SignatureState getState() {
        return (SignatureState) super.getState();
    }

    @Override
    public void updateSignature(SignatureData imageData) {
        // TODO: send imageData to client
    }

    public void addSaveListener(SaveListener saveListener) {
        this.saveListener = saveListener;
    }

    public void addCancelListener(CancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    protected void saveSignature(SignatureData imageData) {
        if (null != saveListener) {
            saveListener.onSave(imageData);
        }

        if (null != signatureId) {
            Broadcaster.broadcast(signatureId, imageData);
        }
    }

    protected void cancelSigning() {
        if (null != cancelListener) {
            cancelListener.onCancel();
        }
    }
}