package eu.michaelvogt.vaadin.addon.signature.server;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

import eu.michaelvogt.vaadin.addon.signature.server.field.FieldUI;
import eu.michaelvogt.vaadin.addon.signature.server.sign.SignUI;

public class SignatureProvider extends UIProvider {

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        String signing = event.getRequest().getParameter("signing");
        if ("fullscreen".equals(signing)) {
            return SignUI.class;
        } else {
            return FieldUI.class;
        }
    }
}
