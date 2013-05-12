package eu.michaelvogt.vaadin.addon.signature.server;

import eu.michaelvogt.vaadin.addon.signature.shared.SignatureData;

public interface BroadcastListener {
    void updateSignature(SignatureData imageData);
}
