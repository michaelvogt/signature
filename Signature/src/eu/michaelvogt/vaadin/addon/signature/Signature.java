package eu.michaelvogt.vaadin.addon.signature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

import eu.michaelvogt.vaadin.addon.signature.SignatureUI.SaveListener;
import eu.michaelvogt.vaadin.addon.signature.client.signature.SignatureServerRpc;
import eu.michaelvogt.vaadin.addon.signature.client.signature.SignatureState;

public class Signature extends AbstractComponentContainer {
    public static final String SIGNATURE_STYLE = "signature";

    private List<Component> children = new ArrayList<Component>();

    private Button redoButton;
    private SaveListener saveListener;

    private SignatureServerRpc rpc = new SignatureServerRpcImpl();

    public Signature() {
        buildMainLayout();
        registerRpc(rpc);
    }

    @Override
    public SignatureState getState() {
        return (SignatureState) super.getState();
    }

    public void addSaveListener(SaveListener saveListener) {
        this.saveListener = saveListener;
    }

    @Override
    public void addComponent(Component c) {
        children.add(c);
        super.addComponent(c);
        markAsDirty();
    }

    @Override
    public void removeComponent(Component c) {
        children.remove(c);
        super.removeComponent(c);
        markAsDirty();
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        int index = children.indexOf(oldComponent);
        if (index != -1) {
            children.remove(index);
            children.add(index, newComponent);
            fireComponentDetachEvent(oldComponent);
            fireComponentAttachEvent(newComponent);
            markAsDirty();
        }
    }

    @Override
    public int getComponentCount() {
        return children.size();
    }

    @Override
    public Iterator<Component> iterator() {
        return children.iterator();
    }

    private void buildMainLayout() {
        redoButton = new Button("Redo");
        redoButton.addClickListener(new RedoSignatureListener());
    }

    private class RedoSignatureListener implements ClickListener {
        @Override
        public void buttonClick(ClickEvent event) {
            // Check if redo is allowed, and notify the client when it is
            removeComponent(redoButton);
            getState().isEditing = true;
        }
    }

    private class SignatureServerRpcImpl implements SignatureServerRpc {
        @Override
        public void saveSignature(String imageData) {
            addComponent(redoButton);
            getState().isEditing = false;

            if (null != saveListener) {
                saveListener.onSave(imageData);
            }
        }
    }
}
