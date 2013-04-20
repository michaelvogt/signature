package eu.michaelvogt.vaadin.addon.signature.client.signature;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.shared.ui.Connect;

import eu.michaelvogt.vaadin.addon.signature.Signature;

@Connect(Signature.class)
public class SignatureConnector extends AbstractComponentContainerConnector {

    SignatureServerRpc rpc = RpcProxy.create(SignatureServerRpc.class, this);

    public SignatureConnector() {
        getWidget().addSaveHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                rpc.saveSignature(getWidget().getSignatureData());
            }
        });
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(SignatureWidget.class);
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

        // TODO: Update state of Widget
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // TODO Auto-generated method stub

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
