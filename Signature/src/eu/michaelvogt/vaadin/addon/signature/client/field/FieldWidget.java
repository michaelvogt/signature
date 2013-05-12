package eu.michaelvogt.vaadin.addon.signature.client.field;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.michaelvogt.vaadin.addon.signature.client.SignatureWidget;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureData;

public class FieldWidget extends SignatureWidget {
    private static FieldUiBinder uiBinder = GWT.create(FieldUiBinder.class);

    interface FieldUiBinder extends UiBinder<Widget, FieldWidget> {
    }

    private SignatureData imageCache;

    public FieldWidget() {
        if (Canvas.isSupported()) {
            signaturWidget = (HTMLPanel) uiBinder.createAndBindUi(this);
            super.add(signaturWidget);

            setupWidget();
        }
    }

    @Override
    public void setIsEditing(boolean isEditing) {
        super.setIsEditing(isEditing);

        if (isEditing) {
            imageCache = getSignatureData();
        }
    }

    @Override
    protected void setupWidget() {
        super.setupWidget();

        clearButton.addClickHandler(new ClearHandler());
        saveButton.addClickHandler(new SaveHandler());
        cancelButton.addClickHandler(new CancelHandler());
    }

    @Override
    public void clear() {
        for (Widget widget : externalWidgets) {
            widget.removeFromParent();
        }
        externalWidgets.clear();
    }

    @Override
    public void add(Widget widget) {
        signaturWidget.add(widget);
        externalWidgets.add(widget);
    }

    private class SaveHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            setIsEditing(false);
        }
    }

    private class CancelHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            setSignature(imageCache.getDataUrl());
            setIsEditing(false);
        }
    }
}