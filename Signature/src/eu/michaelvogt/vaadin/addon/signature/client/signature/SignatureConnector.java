package eu.michaelvogt.vaadin.addon.signature.client.signature;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.customfield.CustomFieldConnector;
import com.vaadin.shared.ui.Connect;

import eu.michaelvogt.vaadin.addon.signature.Signature;
import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureServerRpc;
import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureState;

@Connect(Signature.class)
public class SignatureConnector extends CustomFieldConnector {

    SignatureServerRpc rpc = RpcProxy.create(SignatureServerRpc.class, this);

    public SignatureConnector() {
        getWidget().addSaveHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                rpc.saveSignature(getWidget().getSignatureData());
            }
        });

        getWidget().addCancelHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                rpc.cancelSigning();
            }
        });
    }

    @Override
    public SignatureWidget getWidget() {
        return (SignatureWidget) super.getWidget();
    }

    @Override
    public SignatureState getState() {
        return (SignatureState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (getState().readOnly) {
            getWidget().setIsEditing(false);
        } else {
            getWidget().setIsEditing(getState().isEditing);
        }
    }

    @Override
    public void onConnectorHierarchyChange(
            ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
        List<ComponentConnector> children = getChildComponents();
        SignatureWidget widget = getWidget();
        widget.clear();
        for (ComponentConnector connector : children) {
            widget.add(connector.getWidget());
        }
    }
}
