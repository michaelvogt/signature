package eu.michaelvogt.vaadin.addon.signature.client.signature;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VCustomComponent;

import eu.michaelvogt.vaadin.addon.signature.shared.signature.SignatureData;

public class SignatureWidget extends VCustomComponent {
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

    private HandlerRegistration startTouchHandler;
    private HandlerRegistration stopTouchHandler;

    private HandlerRegistration drawHandler;

    private EventHandler stopHandler;
    private EventHandler startHandler;

    private SignatureData imageCache;

    public SignatureWidget() {
        panel = new FlowPanel();
        super.add(panel);

        setStyleName(CLASSNAME);

        startHandler = new StartHandler();
        stopHandler = new StopHandler();

        if (Canvas.isSupported()) {
            setupWidget();
        } else {
            panel.add(new Label("No Canvas available"));
        }
    }

    public void addSaveHandler(ClickHandler handler) {
        saveButton.addClickHandler(handler);
    }

    public void addCancelHandler(ClickHandler handler) {
        cancelButton.addClickHandler(handler);
    }

    public SignatureData getSignatureData() {
        return new SignatureData(canvas.toDataUrl());
    }

    public void setIsEditing(boolean isEditable) {
        if (isEditable) {
            imageCache = getSignatureData();

            startDrawHandler = canvas
                    .addMouseDownHandler((MouseDownHandler) startHandler);
            startTouchHandler = canvas
                    .addTouchStartHandler((TouchStartHandler) startHandler);
            stopDrawHandler = canvas
                    .addMouseUpHandler((MouseUpHandler) stopHandler);
            stopTouchHandler = canvas
                    .addTouchEndHandler((TouchEndHandler) stopHandler);

            panel.add(cancelButton);
            panel.add(clearButton);
            panel.add(saveButton);
        } else {
            if (null != startDrawHandler) {
                startDrawHandler.removeHandler();
                startDrawHandler = null;

                startTouchHandler.removeHandler();
                startTouchHandler = null;
            }

            if (null != stopDrawHandler) {
                stopDrawHandler.removeHandler();
                stopDrawHandler = null;

                stopTouchHandler.removeHandler();
                stopTouchHandler = null;
            }

            panel.remove(cancelButton);
            panel.remove(clearButton);
            panel.remove(saveButton);
        }
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
        panel.add(widget);
        externalWidgets.add(widget);
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
        saveButton.addClickHandler(new SaveHandler());

        cancelButton = new VButton();
        cancelButton.setText("Cancel");
        cancelButton.addStyleName(CLASSNAME + "-cancel");
        cancelButton.addClickHandler(new CancelHandler());
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

    private class DrawHandler implements MouseMoveHandler, TouchMoveHandler {
        @Override
        public void onMouseMove(MouseMoveEvent event) {
            canvasContext
                    .lineTo(((MouseEvent<?>) event).getX(), (event).getY());
            canvasContext.stroke();
        }

        @Override
        public void onTouchMove(TouchMoveEvent event) {
            JsArray<Touch> touches = event.getTouches();
            if (0 != touches.length()) {
                Touch touch = touches.get(0);
                canvasContext.lineTo(touch.getClientX(), touch.getClientY());
                canvasContext.stroke();
                event.preventDefault();
            }
        }
    }

    private class ClearHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            clearCanvas();
        }
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

    private class StopHandler implements MouseUpHandler, TouchEndHandler {
        @Override
        public void onMouseUp(MouseUpEvent event) {
            removeHandler();
        }

        @Override
        public void onTouchEnd(TouchEndEvent event) {
            removeHandler();
        }

        private void removeHandler() {
            drawHandler.removeHandler();
        }
    }

    private class StartHandler implements MouseDownHandler, TouchStartHandler {
        @Override
        public void onMouseDown(MouseDownEvent event) {
            canvasContext.beginPath();
            canvasContext.moveTo(event.getX(), event.getY());
            drawHandler = canvas.addMouseMoveHandler(new DrawHandler());
        }

        @Override
        public void onTouchStart(TouchStartEvent event) {
            canvasContext.beginPath();

            JsArray<Touch> touches = event.getTouches();
            if (0 != touches.length()) {
                Touch touch = touches.get(0);
                canvasContext.moveTo(touch.getClientX(), touch.getClientY());
                drawHandler = canvas.addMouseMoveHandler(new DrawHandler());
            }
        }
    }
}