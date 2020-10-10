package tv.lid.cinema.api.storages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import tv.lid.cinema.api.models.CommonModel;

// базовый абстрактный класс хранилища данных
public abstract class CommonStorage<T extends CommonModel> {
    // хранимые данные
    protected final ArrayList<T> dataList;

    // класс хранимых данных
    protected final Class<T> dataClass;

    // конструктор
    protected CommonStorage(final Class<T> dataClass) {
        this.dataList  = new ArrayList<T>();
        this.dataClass = dataClass;
    }

    // считать все данные
    public abstract void readAll();

    // записать все данные
    public abstract void writeAll();

    // найти запись по заданному идентификатору
    public final T get(final long timestamp) {
        return this.dataList
            .stream()
            .filter((T item) -> item.timestamp == timestamp)
            .findFirst()
            .orElse(null);
    }

    // добавить или изменить запись
    public final void set(final T dataItem) {
        final int dataIndex = IntStream
            .range(0, this.dataList.size())
            .filter((int index) -> this.dataList.get(index).timestamp == dataItem.timestamp)
            .findFirst()
            .orElse(-1);

        if (dataIndex == -1) { // новая запись
            this.dataList.add(dataItem);
        } else { // уже существующая запись
            this.dataList.set(dataIndex, dataItem);
        }
    }

    // получить список всех записей
    public final List<T> list() {
        return Collections.unmodifiableList(this.dataList);
    }

    // удалить поле с заданным идентификатором
    public final void kill(final long timestamp) {
        final int dataIndex = IntStream
            .range(0, this.dataList.size())
            .filter((int index) -> this.dataList.get(index).timestamp == timestamp)
            .findFirst()
            .orElse(-1);
        if (dataIndex != -1) {
            this.dataList.remove(dataIndex);
        }
    }
}
