package eu.michaelvogt.vaadin.addon.signature.client;

import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.customfield.CustomFieldConnector;

import eu.michaelvogt.vaadin.addon.signature.shared.SignatureServerRpc;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureState;

public abstract class SignatureConnector extends CustomFieldConnector {
    protected SignatureServerRpc rpc = RpcProxy.create(
            SignatureServerRpc.class, this);

    @Override
    public SignatureState getState() {
        return (SignatureState) super.getState();
    }
}
