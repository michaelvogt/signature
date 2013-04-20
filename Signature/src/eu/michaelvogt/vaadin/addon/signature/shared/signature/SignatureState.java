package eu.michaelvogt.vaadin.addon.signature.shared.signature;

public class SignatureState extends com.vaadin.shared.AbstractComponentState {
    /**
     * The base64 data of the signature as received from canvas
     */
    public String signature;

    /**
     * Defines the editing state of the Widget
     */
    public boolean isEditable = false;

}