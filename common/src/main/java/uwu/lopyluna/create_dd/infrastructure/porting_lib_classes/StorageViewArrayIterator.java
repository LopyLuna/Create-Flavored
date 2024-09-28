package uwu.lopyluna.create_dd.infrastructure.porting_lib_classes;

import uwu.lopyluna.create_dd.infrastructure.fabric_classes.StorageView;

import java.util.Iterator;

public class StorageViewArrayIterator<T> implements Iterator<StorageView<T>> {
    protected final StorageView<T>[] views;
    protected int index = 0;

    public StorageViewArrayIterator(StorageView<T>[] views) {
        this.views = views;
    }

    @Override
    public boolean hasNext() {
        return index < views.length;
    }

    @Override
    public StorageView<T> next() {
        return views[index++];
    }
}
