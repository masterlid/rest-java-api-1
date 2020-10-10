package tv.lid.cinema.api;

import spark.Spark;
import static spark.Spark.*;

import tv.lid.cinema.api.controllers.ScheduleController;
import tv.lid.cinema.api.models.ScheduleModel;
import tv.lid.cinema.api.storages.FileStorage;

import static tv.lid.cinema.api.transformers.JsonTransformer.JSON;

// класс приложения
public final class App {
    public  static final String JSON_MIME_TYPE = "application/json";

    private static final String SERVER_HOST    = "127.0.0.1";
    private static final int    SERVER_PORT    = 8808;

    private static final String DEFAULT_FILE   = "schedule.tsv";

    public static void main(final String[] args) {
        // создание файлового хранилища
        final FileStorage<ScheduleModel> storage = new FileStorage<ScheduleModel>(
            args != null && args.length == 1 ? args[0] : DEFAULT_FILE,
            ScheduleModel.class
        );
        // чтение файла данных
        storage.readAll();

        // создание контроллера
        final ScheduleController controller = new ScheduleController(storage);

        // конфигурация сервера
        ipAddress(SERVER_HOST);
        port(SERVER_PORT);

        // объявление роутов
        path("/api", () -> {
            get(    "/schedule",            controller.list,           JSON );
            get(    "/schedule/:timestamp", controller.find,           JSON );
            post(   "/schedule",            controller.createOrModify, JSON );
            put(    "/schedule",            controller.createOrModify, JSON );
            delete( "/schedule/:timestamp", controller.kill,           JSON );
        });
        after((req, res) -> {
            res.type(JSON_MIME_TYPE);
        });

        // хук завершения работы
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // останов сервера
                Spark.stop();
                // сохранение данных в файл
                storage.writeAll();
            }
        });
    }
}
