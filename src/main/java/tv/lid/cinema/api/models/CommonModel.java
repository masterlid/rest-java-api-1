package tv.lid.cinema.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// базовый абстрактный класс модели
public abstract class CommonModel {
    // идентификатор записи
    @JsonProperty(value = "timestamp", required = false, defaultValue = "0")
    public final long timestamp;

    // конструктор #1
    @JsonCreator
    protected CommonModel(@JsonProperty("timestamp") final long timestamp) {
        this.timestamp = timestamp == 0 ? (System.currentTimeMillis() / 1000) : timestamp;
    }

    // конструктор #2
    protected CommonModel() {
        this(0);
    }

    // конструктор #3
    public CommonModel(final String[] fields) throws IllegalArgumentException {
        if (fields != null && fields.length > 1) {
            try {
                this.timestamp = Long.parseLong(fields[0]); // идентификатор
            } catch (NumberFormatException exc) {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    // метод переводит данную модель в массив строк для преобразования в TSV
    public abstract String[] toFields();
}
