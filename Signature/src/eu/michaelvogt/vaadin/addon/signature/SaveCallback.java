package eu.michaelvogt.vaadin.addon.signature;

import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureData;

public interface SaveCallback {
    void onSave(SignatureData imageData);
}
