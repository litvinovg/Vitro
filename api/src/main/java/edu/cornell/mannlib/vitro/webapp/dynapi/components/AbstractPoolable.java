package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractPoolable implements AutoCloseable {

    private static final Log log = LogFactory.getLog(AbstractPoolable.class);

    private Set<Long> clients = ConcurrentHashMap.newKeySet();

    private String uri;
    
    public void addClient() {
        clients.add(Thread.currentThread().getId());
    }
    
    public void removeClient() {
        clients.remove(Thread.currentThread().getId());
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
