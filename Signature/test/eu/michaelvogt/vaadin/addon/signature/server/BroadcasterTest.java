package eu.michaelvogt.vaadin.addon.signature.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import eu.michaelvogt.vaadin.addon.signature.shared.SignatureData;

public class BroadcasterTest {
    UUID signatureId1 = UUID.randomUUID();
    BroadcastListener listener1;

    UUID signatureId2 = UUID.randomUUID();
    BroadcastListener listener2;

    BroadcastListener listener3;

    @Before
    public void setup() {
        Broadcaster.clear();

        listener1 = new BroadcastListenerImpl();
        listener2 = new BroadcastListenerImpl();
        listener3 = new BroadcastListenerImpl();
    }

    @Test
    public void testRegister() {
        Broadcaster.register(signatureId1, listener1);
        assertEquals(1, Broadcaster.getListeners(signatureId1).size());

        // Make sure the same listener does not get added twice
        Broadcaster.register(signatureId1, listener1);
        assertEquals(1, Broadcaster.getListeners(signatureId1).size());

        // Add second listener to same signature
        Broadcaster.register(signatureId1, listener2);
        assertEquals(2, Broadcaster.getListeners(signatureId1).size());

        // Add a listener to a different signature
        Broadcaster.register(signatureId2, listener3);
        assertEquals(2, Broadcaster.getListeners(signatureId1).size());
        assertEquals(1, Broadcaster.getListeners(signatureId2).size());
    }

    @Test
    public void testRemove() {
        Broadcaster.register(signatureId1, listener1);
        Broadcaster.register(signatureId1, listener2);
        assertEquals(2, Broadcaster.getListeners(signatureId1).size());

        Broadcaster.register(signatureId2, listener3);
        assertEquals(1, Broadcaster.getListeners(signatureId2).size());

        Broadcaster.unregister(signatureId1, listener2);
        assertEquals(1, Broadcaster.getListeners(signatureId1).size());
        assertEquals(listener1, Broadcaster.getListeners(signatureId1)
                .iterator().next());
        assertEquals(1, Broadcaster.getListeners(signatureId2).size());
    }

    @Test
    public void testBroadcast() throws InterruptedException {
        Broadcaster.register(signatureId1, listener1);
        Broadcaster.register(signatureId1, listener2);
        assertEquals(2, Broadcaster.getListeners(signatureId1).size());

        Broadcaster.register(signatureId2, listener3);
        assertEquals(1, Broadcaster.getListeners(signatureId2).size());

        Broadcaster.broadcast(signatureId1, null);
        Thread.sleep(100);

        assertTrue(((BroadcastListenerImpl) listener1).wasCalled());
        assertFalse(((BroadcastListenerImpl) listener3).wasCalled());
    }

    private class BroadcastListenerImpl implements BroadcastListener {
        private boolean wasCalled;

        @Override
        public void updateSignature(SignatureData imageData) {
            wasCalled = true;
        }

        public boolean wasCalled() {
            return wasCalled;
        }
    }
}
