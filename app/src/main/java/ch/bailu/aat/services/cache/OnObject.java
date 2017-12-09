package ch.bailu.aat.services.cache;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;

/**
 * Created by bailuk on 03.12.17.
 */

public abstract class OnObject {
    public OnObject(final ServiceContext sc, final String id,
                    final ObjectHandle.Factory factory, final Class c) {
        new InsideContext(sc) {

            @Override
            public void run() {

                ObjectHandle handle = sc.getCacheService().getObject(
                        id,
                        factory
                );

                if (c.isInstance(handle)) {
                    OnObject.this.run(handle);
                }
            }
        };

    }

    public OnObject(final ServiceContext sc, final String id, final Class c) {

        new InsideContext(sc) {
            @Override
            public void run() {
                ObjectHandle handle = sc.getCacheService().getObject(id);

                try {
                    if (c.isInstance(handle))
                        OnObject.this.run(handle);

                } finally {
                    handle.free();
                }
            }
        };
    }

    public abstract void run(ObjectHandle handle);
}
