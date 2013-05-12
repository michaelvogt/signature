package eu.michaelvogt.vaadin.addon.signature.client.sign;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.michaelvogt.vaadin.addon.signature.client.SignatureWidget;

public class SignWidget extends SignatureWidget {
    private static SignUiBinder uiBinder = GWT.create(SignUiBinder.class);

    interface SignUiBinder extends UiBinder<Widget, SignWidget> {
    }

    public SignWidget() {
        if (Canvas.isSupported()) {
            signaturWidget = (HTMLPanel) uiBinder.createAndBindUi(this);
            super.add(signaturWidget);

            setupWidget();
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
            setIsEditing(false);
        }
    }
}
