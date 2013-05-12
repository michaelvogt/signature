package eu.michaelvogt.vaadin.addon.signature.server.sign;

import com.vaadin.ui.CssLayout;

import eu.michaelvogt.vaadin.addon.signature.server.Signature;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureData;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureServerRpc;

public class Sign extends Signature {
    public static final String SIGNATURE_STYLE = "signature";

    private SignatureServerRpc rpc = new SignatureServerRpcImpl();

    public Sign() {
        layout = new CssLayout();

        getState().isEditing = true;

        registerRpc(rpc);
    }

    private class SignatureServerRpcImpl implements SignatureServerRpc {
        @Override
        public void saveSignature(SignatureData imageData) {
            Sign.this.saveSignature(imageData);
        }

        @Override
        public void cancelSigning() {
            Sign.this.cancelSigning();
        }
    }
}
