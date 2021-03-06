package com.hendalqett.products.otto;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public final class BusProvider {
    private static final Bus bus = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return bus;
    }

    private BusProvider() {
        // No instances.
    }
}