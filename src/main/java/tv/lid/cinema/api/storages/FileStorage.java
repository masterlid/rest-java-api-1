package tv.lid.cinema.api.storages;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import tv.lid.cinema.api.models.CommonModel;

// класс файлового хранилища данных
public final class FileStorage<T extends CommonModel> extends CommonStorage<T> {
    // разделитель полей в строке
    private static final String FIELD_SEPARATOR = "\t";

    // имя файла для хранения данных
    private final String fileName;

    // конструктор
    public FileStorage(final String fileName, final Class<T> dataClass) {
        super(dataClass);

        this.fileName = fileName;
    }

    // считать все данные из файла
    public void readAll() {
        this.dataList.clear();

        final File file = new File(this.fileName);
        if (file.exists() && file.isFile() && file.canRead()) {
            try {
                final RandomAccessFile raf = new RandomAccessFile(file, "r");
                String utf;

                while((utf = raf.readUTF()) != null) {
                    try {
                        this.dataList.add(
                            dataClass.getDeclaredConstructor(String[].class).newInstance(
                                new Object[] { utf.split(FIELD_SEPARATOR) }
                            )
                        );
                    } catch (Exception exc) {}
                }
                raf.close();
            } catch (IOException exc) {
            }
        }
    }

    // записать все данные в файл
    public void writeAll() {
        final File file = new File(this.fileName);
        try {
            final RandomAccessFile raf = new RandomAccessFile(file, "rw");

            raf.setLength(0);
            this.dataList.forEach((T dataItem) -> {
                try {
                    raf.writeUTF(String.join(FIELD_SEPARATOR, dataItem.toFields()));
                } catch (IOException exc) {}
            });
            raf.close();
        } catch (IOException exc) {
        }
    }
}
