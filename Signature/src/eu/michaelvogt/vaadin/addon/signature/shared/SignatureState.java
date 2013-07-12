package eu.michaelvogt.vaadin.addon.signature.shared;

import com.vaadin.shared.AbstractFieldState;

public class SignatureState extends AbstractFieldState {
    /**
     * Defines the editing state of the Widget
     */
    public boolean isEditing = false;

    /**
     * The Signature that is currently displayed
     */
    public SignatureData imageData;

}