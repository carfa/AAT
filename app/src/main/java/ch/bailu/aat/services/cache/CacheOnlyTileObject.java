package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.util_java.foc.Foc;

public class CacheOnlyTileObject extends TileObject {

    private final Tile tile;


    private final SyncTileBitmap bitmap = new SyncTileBitmap();

    private final FileHandle load;
    private final Foc file;

    private final boolean isTransparent;



    public CacheOnlyTileObject(String id, ServiceContext sc, Tile t, final boolean transparent) {
        super(id);

        file = FocAndroid.factory(sc.getContext(), id);
        tile = t;

        sc.getCacheService().addToBroadcaster(this);

        isTransparent = transparent;
        load = new TileLoaderHandle(file);




    }


    public static class TileLoaderHandle extends FileHandle {

        public TileLoaderHandle(Foc f) {
            super(f);
        }

        @Override
        public long bgOnProcess(final ServiceContext sc) {

            final long[] size = {0};


            new OnObject(sc, getFile().getPath(), CacheOnlyTileObject.class) {
                @Override
                public void run(ObjectHandle handle) {
                    CacheOnlyTileObject tile = (CacheOnlyTileObject) handle;

                    tile.bitmap.set(getFile(), TILE_SIZE, tile.isTransparent);


                    AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE,
                            getFile());

                    size[0] =  tile.bitmap.getSize();


                }
            };
            return size[0];


        }
    };

    @Override
    public TileBitmap getTileBitmap() {
        return bitmap.getTileBitmap();
    }

    @Override
    public Tile getTile() {
        return tile;
    }

    @Override
    public void onInsert(ServiceContext sc) {
        if (isLoadable()) sc.getBackgroundService().process(load);
    }


    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }

    @Override
    public void reDownload(ServiceContext sc) {
    }

    @Override
    public boolean isLoaded() {
        return getBitmap() != null;
    }


    private boolean isLoadable() {
        return file.isFile() && file.canRead();
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {}

    @Override
    public void onChanged(String id, ServiceContext sc) {
    }


    public boolean isReadyAndLoaded() {
        boolean loaded = isLoaded();
        boolean notLoadable = isLoadable() == false;

        return loaded || notLoadable;
    }


    @Override
    public long getSize() {
        return bitmap.getSize();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap.getAndroidBitmap();
    }


    public static class Factory extends ObjectHandle.Factory {
        private final Tile mapTile;
        private final boolean isTransparent;


        public Factory(Tile mt, Boolean t) {
            mapTile = mt;
            isTransparent = t;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new CacheOnlyTileObject(id, cs, mapTile, isTransparent);
        }
    }
}

