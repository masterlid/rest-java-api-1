package tv.lid.cinema.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// класс модели сеанса
public final class ScheduleModel extends CommonModel {
    // название кинофильма
    @JsonProperty(value = "movieTitle", required = true)
    public final String movieTitle;

    // дата и время начала
    @JsonProperty(value = "dateAndTime", required = true)
    public final String dateAndTime;

    // длительность сеанса в минутах
    @JsonProperty(value = "movieDuration", required = true)
    public final int movieDuration;

    // номер зала
    @JsonProperty(value = "auditorium", required = false, defaultValue = "1")
    public final int auditorium;

    // конструктор #1 -- используется для создания экземпляра из входящего запроса
    @JsonCreator
    public ScheduleModel(
        @JsonProperty("timestamp")     final long   timestamp,
        @JsonProperty("movieTitle")    final String movieTitle,
        @JsonProperty("dateAndTime")   final String dateAndTime,
        @JsonProperty("movieDuration") final int    movieDuration,
        @JsonProperty("auditorium")    final int    auditorium
    ) {
        super(timestamp);

        this.movieTitle    = movieTitle;
        this.dateAndTime   = dateAndTime;
        this.movieDuration = movieDuration;
        this.auditorium    = auditorium;
    }

    // конструктор #2 -- используется для создания экземпляра при чтении файла
    public ScheduleModel(final String[] fields) throws IllegalArgumentException {
        super(fields);

        if (fields != null && fields.length == 5) {
            try {
                this.movieTitle    = fields[1];                   // название кинофильма
                this.dateAndTime   = fields[2];                   // дата и время сеанса
                this.movieDuration = Integer.parseInt(fields[3]); // длительность сеанса в минутах
                this.auditorium    = Integer.parseInt(fields[4]); // номер зала
            } catch (NumberFormatException exc) {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    // метод переводит данную модель в массив строк для преобразования в TSV
    public String[] toFields() {
        return new String [] {
            String.valueOf(this.timestamp),
            this.movieTitle,
            this.dateAndTime,
            String.valueOf(this.movieDuration),
            String.valueOf(this.auditorium)
        };
    }
}
