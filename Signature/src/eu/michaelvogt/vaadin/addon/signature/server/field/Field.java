package eu.michaelvogt.vaadin.addon.signature.server.field;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import eu.michaelvogt.vaadin.addon.signature.server.SaveListener;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureData;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureServerRpc;
import eu.michaelvogt.vaadin.addon.signature.shared.SignatureState;

public class Field extends CustomField<SignatureData> {
    public static final String SIGNATURE_STYLE = "signature";
    public static final String IMAGE_URL = "signatureqrcode.png";

    private CssLayout layout;
    private Button editButton;
    private Button externButton;
    private SaveListener saveListener;
    private boolean isExternalAllowed;

    private SignatureServerRpc rpc = new SignatureServerRpcImpl();

    private final RequestHandler requestHandler = new RequestHandler() {
        @Override
        public boolean handleRequest(VaadinSession session,
                VaadinRequest request, VaadinResponse response)
                throws IOException {
            if (("/" + IMAGE_URL).equals(request.getPathInfo())) {
                BitMatrix qrMatrix;
                String content = request.getParameter("content");

                try {
                    qrMatrix = new QRCodeWriter().encode(content,
                            BarcodeFormat.QR_CODE, 100, 100);
                } catch (com.google.zxing.WriterException e) {
                    System.out.println("Could not encode QR Code for '"
                            + content + "'");
                    return true;
                }

                BufferedImage image = MatrixToImageWriter
                        .toBufferedImage(qrMatrix);
                response.setContentType("image/png");
                ImageIO.write(image, "png", response.getOutputStream());

                return true;
            }
            // If the URL did not match our image URL, let the other request
            // handlers handle it
            return false;
        }
    };

    public Field() {
        UI.getCurrent().getSession().addRequestHandler(requestHandler);

        buildMainLayout();
        registerRpc(rpc);
    }

    @Override
    protected Component initContent() {
        return layout;
    }

    @Override
    public Class<SignatureData> getType() {
        return SignatureData.class;
    }

    @Override
    public SignatureState getState() {
        return (SignatureState) super.getState();
    }

    @Override
    public void detach() {
        super.detach();
        getSession().removeRequestHandler(requestHandler);
    }

    public void addSaveListener(SaveListener saveListener) {
        this.saveListener = saveListener;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        if (readOnly) {
            getState().isEditing = false;
            layout.removeComponent(editButton);
            layout.removeComponent(externButton);
        } else {
            layout.addComponent(editButton);
            if (isExternalAllowed) {
                layout.addComponent(externButton);
            }
        }
    }

    public void allowExternalSigning(boolean isAllowed) {
        isExternalAllowed = isAllowed;

        if (isAllowed) {
            layout.addComponent(externButton);
        } else if (!isAllowed) {
            layout.removeComponent(externButton);
        }
    }

    private void buildMainLayout() {
        layout = new CssLayout();

        editButton = new Button("Edit");
        editButton.addClickListener(new EditSignatureListener());
        layout.addComponent(editButton);

        externButton = new Button("Sign with Touch");
        externButton.addClickListener(new ExternSignatureListener());
    }

    private class EditSignatureListener implements ClickListener {
        @Override
        public void buttonClick(ClickEvent event) {
            layout.removeComponent(editButton);
            getState().isEditing = true;
        }
    }

    private class ExternSignatureListener implements ClickListener {
        @Override
        public void buttonClick(ClickEvent event) {
            // Create specific url for sign ui
            UUID key = UUID.randomUUID();
            String thismobileURL = Page.getCurrent().getLocation().toString()
                    + "#" + key;

            // Show code on top of signature field
            Window qr = new Window();
            qr.setContent(new Image(null, new ExternalResource(IMAGE_URL
                    + "?content=" + thismobileURL)));
            UI.getCurrent().addWindow(qr);

            // When url is accessed, show full screen canvas ui
            // When saved, send to original ui
        }
    }

    private class SignatureServerRpcImpl implements SignatureServerRpc {
        @Override
        public void saveSignature(SignatureData imageData) {
            layout.addComponent(editButton);
            getState().isEditing = false;
            setValue(imageData);

            if (null != saveListener) {
                saveListener.onSave(imageData);
            }
        }

        @Override
        public void cancelSigning() {
            layout.addComponent(editButton);
            getState().isEditing = false;
        }
    }
}
