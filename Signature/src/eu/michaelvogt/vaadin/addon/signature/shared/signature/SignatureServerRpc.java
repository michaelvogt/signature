package eu.michaelvogt.vaadin.addon.signature.shared.signature;

import com.vaadin.shared.communication.ServerRpc;

public interface SignatureServerRpc extends ServerRpc {
    public void saveSignature(SignatureData imageData);

    public void cancelSigning();
}
