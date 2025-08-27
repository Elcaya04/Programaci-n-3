package service_layer;

import utilites.ChangeType;

public interface ServiceObserver<T> {
    void DataChanged(ChangeType type, T entity);
}
