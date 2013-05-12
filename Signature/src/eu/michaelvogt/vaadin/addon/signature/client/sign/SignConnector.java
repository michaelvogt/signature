package eu.michaelvogt.vaadin.addon.signature.client.sign;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

import eu.michaelvogt.vaadin.addon.signature.client.SignatureConnector;
import eu.michaelvogt.vaadin.addon.signature.server.sign.Sign;

@Connect(Sign.class)
public class SignConnector extends SignatureConnector {
    public SignConnector() {
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
    public SignWidget getWidget() {
        return (SignWidget) super.getWidget();
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
        SignWidget widget = getWidget();
        widget.clear();
        for (ComponentConnector connector : children) {
            widget.add(connector.getWidget());
        }
    }
}
