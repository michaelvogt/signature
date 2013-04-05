package eu.michaelvogt.vaadin.addon.signature;

import org.vaadin.hezamu.canvas.Canvas;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;

public class Signature extends CustomComponent {
    public static final String SIGNATURE_STYLE = "signature";

    private CssLayout mainLayout;
    private Canvas canvas;
    private Image signature;

    private Button signButton;
    private Button clearButton;

    private boolean isFix;

    public Signature() {
        buildMainLayout();
        setCompositionRoot(mainLayout);

        // placeholder for signature
        signature.setSource(new ExternalResource("VAADIN/signature.jpg"));
    }

    public void setSignature(Image signature) {
        this.signature = signature;

        showSignature();
    }

    public void fixSignature(boolean isFix) {
        this.isFix = isFix;
    }

    private void buildMainLayout() {
        mainLayout = new CssLayout();
        mainLayout.setStyleName(SIGNATURE_STYLE);

        signButton = new Button("Sign");
        signButton.addClickListener(new SaveSignatureListener());
        mainLayout.addComponent(signButton);

        clearButton = new Button("Clear");
        clearButton.addClickListener(new ClearSignatureListener());
        mainLayout.addComponent(clearButton);

        signature = new Image();

        showCanvas();
    }

    private void showSignature() {
        mainLayout.removeComponent(canvas);
        mainLayout.addComponent(signature);

        signButton.setVisible(false);
        clearButton.setVisible(!isFix);
    }

    private void showCanvas() {
        if (null != signature) {
            mainLayout.removeComponent(signature);
        }

        canvas = new Canvas();
        canvas.setSizeFull();

        canvas.setFillStyle("#000000");
        canvas.fillText("Please sign here", 10, 10, canvas.getWidth());

        mainLayout.addComponent(canvas);

        signButton.setVisible(true);
        clearButton.setVisible(true);
    }

    private class ClearSignatureListener implements ClickListener {
        @Override
        public void buttonClick(ClickEvent event) {
            showCanvas();
        }
    }

    private class SaveSignatureListener implements ClickListener {
        @Override
        public void buttonClick(ClickEvent event) {
            // get the pixeldata from the canvas
            // store in image component
            // switch to image component
            showSignature();
        }
    }
}