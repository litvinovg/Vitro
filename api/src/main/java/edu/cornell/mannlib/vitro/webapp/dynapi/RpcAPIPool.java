package edu.cornell.mannlib.vitro.webapp.dynapi;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.RPC;

public class RpcAPIPool extends AbstractPool<String, RPC, RpcAPIPool> {

    private static RpcAPIPool INSTANCE = new RpcAPIPool();

    public static RpcAPIPool getInstance() {
        return INSTANCE;
    }

    @Override
    public RpcAPIPool getPool() {
        return getInstance();
    }

    @Override
    public RPC getDefault() {
        return NullRPC.getInstance();
    }

    @Override
    public Class<RPC> getType() {
        return RPC.class;
    }

}
