package eu.michaelvogt.vaadin.addon.signature.client.signature;

import com.vaadin.shared.communication.ServerRpc;

public interface SignatureServerRpc extends ServerRpc {
    public void saveSignature(String imageData);
}
