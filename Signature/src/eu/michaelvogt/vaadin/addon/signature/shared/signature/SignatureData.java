package eu.michaelvogt.vaadin.addon.signature.shared.signature;

import java.io.Serializable;

public class SignatureData implements Serializable {
    private String dataUrl;

    public SignatureData() {
    }

    public SignatureData(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }
}
