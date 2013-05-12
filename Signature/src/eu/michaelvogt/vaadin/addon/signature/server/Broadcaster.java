package eu.michaelvogt.vaadin.addon.signature.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import eu.michaelvogt.vaadin.addon.signature.shared.SignatureData;

public class Broadcaster {
    private static Map<UUID, Set<BroadcastListener>> signatures = new HashMap<UUID, Set<BroadcastListener>>();

    public synchronized static void register(UUID signatureId,
            BroadcastListener listener) {
        Set<BroadcastListener> signature = signatures.get(signatureId);
        if (null == signature) {
            signatures.put(signatureId, new HashSet<BroadcastListener>());
            signature = signatures.get(signatureId);
        }

        signature.add(listener);
    }

    public synchronized static void unregister(UUID signatureId,
            BroadcastListener listener) {
        Set<BroadcastListener> signature = signatures.get(signatureId);
        if (null != signature) {
            signature.remove(listener);
        }
    }

    public synchronized static Set<BroadcastListener> getListeners(
            UUID signatureId) {
        Set<BroadcastListener> listenerCopy = new HashSet<BroadcastListener>();
        Set<BroadcastListener> signature = signatures.get(signatureId);

        if (null != signature) {
            listenerCopy.addAll(signature);
        }
        return listenerCopy;
    }

    public static void broadcast(UUID signatureId, final SignatureData imageData) {
        final Set<BroadcastListener> listenerCopy = getListeners(signatureId);

        Thread eventThread = new Thread() {
            @Override
            public void run() {
                for (BroadcastListener listener : listenerCopy) {
                    listener.updateSignature(imageData);
                }
            }
        };
        eventThread.start();
    }

    public synchronized static void clear() {
        signatures.clear();
    }
}
