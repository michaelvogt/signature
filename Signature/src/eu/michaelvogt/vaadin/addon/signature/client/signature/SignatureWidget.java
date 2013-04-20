package eu.michaelvogt.vaadin.addon.signature.client.signature;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VButton;

public class SignatureWidget extends Composite {
    public static final String CLASSNAME = "signature";

    private FlowPanel panel;

    private Canvas canvas;
    private Context2d canvasContext;

    private VButton clearButton;
    private VButton saveButton;

    private HandlerRegistration startDrawHandler;
    private HandlerRegistration stopDrawHandler;
    private HandlerRegistration drawHandler;

    private List<Widget> externalWidgets = new ArrayList<Widget>();

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

    private void setupWidget() {
        canvas = Canvas.createIfSupported();
        panel.add(canvas);

        startDrawHandler = canvas.addMouseDownHandler(new StartDrawHandler());
        stopDrawHandler = canvas.addMouseUpHandler(new StopDrawHandler());

        canvasContext = canvas.getContext2d();

        clearButton = new VButton();
        clearButton.setText("Clear");
        clearButton.addStyleName(CLASSNAME + "-clear");
        clearButton.addClickHandler(new ClearHandler());
        panel.add(clearButton);

        saveButton = new VButton();
        saveButton.setText("Save");
        saveButton.addStyleName(CLASSNAME + "-save");
        saveButton.addClickHandler(new SaveHandler());
        panel.add(saveButton);
    }

    private class StopDrawHandler implements MouseUpHandler {
        @Override
        public void onMouseUp(MouseUpEvent event) {
            drawHandler.removeHandler();
        }
    }

    private class StartDrawHandler implements MouseDownHandler {
        @Override
        public void onMouseDown(MouseDownEvent event) {
            canvasContext.beginPath();
            canvasContext.moveTo(event.getX(), event.getY());
            drawHandler = canvas.addMouseMoveHandler(new DrawHandler());
        }
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
            canvasContext.clearRect(0, 0, canvas.getOffsetWidth(),
                    canvas.getOffsetHeight());
        }
    }

    private class SaveHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            saveButton.setVisible(false);
            clearButton.setVisible(false);

            startDrawHandler.removeHandler();
            stopDrawHandler.removeHandler();
        }
    }
}