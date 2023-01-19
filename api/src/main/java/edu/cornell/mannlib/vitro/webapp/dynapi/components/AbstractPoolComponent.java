package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractPoolComponent implements AutoCloseable {

    private static final Log log = LogFactory.getLog(AbstractPoolComponent.class);

    private Set<Long> clients = ConcurrentHashMap.newKeySet();

    private String uri;
    
    public void addClient() {
        final long id = Thread.currentThread().getId();
        log.error(String.format("Client added to %s , client id %s", uri, id));
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            log.error(ste + "\n");
        }
        clients.add(id);
    }
    
    public void removeClient() {
        final long id = Thread.currentThread().getId();
        log.error(String.format("Client removed from %s , client id %s", uri, id));
        clients.remove(id);
    }
    
    public void removeDeadClients() {
        Set<Long> currentClients = new HashSet<Long>();
        currentClients.addAll(clients);
        Map<Long, Boolean> currentThreadIds = Thread
                .getAllStackTraces()
                .keySet()
                .stream()
                .collect(Collectors.toMap(Thread::getId, Thread::isAlive));
        for (Long client : currentClients) {
            if (!currentThreadIds.containsKey(client) || currentThreadIds.get(client) == false) {
                log.error("Removed left client thread with id " + client);
                clients.remove(client);
            }
        }
    }
    
    public boolean hasClients() {
        return !clients.isEmpty();
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
    
    @Override
    public void close() throws Exception {
        removeClient();
    }
    
}
