package eu.michaelvogt.vaadin.addon.signature.client.signature;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VButton;

public class SignatureWidget extends Composite implements MouseDownHandler,
        MouseUpHandler {
    public static final String CLASSNAME = "signature";
    private List<Widget> externalWidgets = new ArrayList<Widget>();

    private FlowPanel panel;
    private Canvas canvas;
    private Context2d canvasContext;

    private VButton clearButton;
    private VButton saveButton;
    private VButton cancelButton;

    private HandlerRegistration startDrawHandler;
    private HandlerRegistration stopDrawHandler;
    private HandlerRegistration drawHandler;

    public SignatureWidget() {
        panel = new FlowPanel();
        initWidget(panel);

        setStyleName(CLASSNAME);

        if (Canvas.isSupported()) {
            setupWidget();
        } else {
            // TODO: Inform user that Canvas is needed. Fallback to something?
        }
    }

    public void addSaveHandler(ClickHandler handler) {
        saveButton.addClickHandler(handler);
    }

    public void addCancelHandler(ClickHandler handler) {
        cancelButton.addClickHandler(handler);
    }

    public String getSignatureData() {
        return canvas.toDataUrl();
    }

    public void clear() {
        for (Widget widget : externalWidgets) {
            widget.removeFromParent();
        }
        externalWidgets.clear();
    }

    public void add(Widget widget) {
        panel.add(widget);
        externalWidgets.add(widget);
    }

    public void setEditable(boolean isEditable) {
        if (isEditable) {
            startDrawHandler = canvas.addMouseDownHandler(this);
            stopDrawHandler = canvas.addMouseUpHandler(this);

            panel.add(cancelButton);
            panel.add(clearButton);
            panel.add(saveButton);
        } else {
            startDrawHandler.removeHandler();
            stopDrawHandler.removeHandler();

            panel.remove(cancelButton);
            panel.remove(clearButton);
            panel.remove(saveButton);
        }
    }

    private void setupWidget() {
        canvas = Canvas.createIfSupported();
        panel.add(canvas);

        canvasContext = canvas.getContext2d();

        clearButton = new VButton();
        clearButton.setText("Clear");
        clearButton.addStyleName(CLASSNAME + "-clear");
        clearButton.addClickHandler(new ClearHandler());

        saveButton = new VButton();
        saveButton.setText("Save");
        saveButton.addStyleName(CLASSNAME + "-save");
        saveButton.addClickHandler(new EditFinishedHandler());

        cancelButton = new VButton();
        cancelButton.setText("Cancel");
        cancelButton.addStyleName(CLASSNAME + "-cancel");
        cancelButton.addClickHandler(new EditFinishedHandler());
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        drawHandler.removeHandler();
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        canvasContext.beginPath();
        canvasContext.moveTo(event.getX(), event.getY());
        drawHandler = canvas.addMouseMoveHandler(new DrawHandler());
    }

    public void setSignature(String signature) {
        clearCanvas();

        ImageElement image = ImageElement.as(DOM.createImg());
        image.setSrc(signature);
        canvasContext.drawImage(image, 0, 0);
    }

    private void clearCanvas() {
        canvasContext.clearRect(0, 0, canvas.getOffsetWidth(),
                canvas.getOffsetHeight());
    }

    private class DrawHandler implements MouseMoveHandler {
        @Override
        public void onMouseMove(MouseMoveEvent event) {
            canvasContext
                    .lineTo(((MouseEvent<?>) event).getX(), (event).getY());
            canvasContext.stroke();
        }
    }

    private class ClearHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            clearCanvas();
        }
    }

    private class EditFinishedHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            setEditable(false);
        }
    }
}