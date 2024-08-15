package task_2;

import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Destination;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Укажи путь к файлу");
            return;
        }

        String filePath = args[0];

        try {
            // Создал соединение
            Connection connection = new ConnectionImpl();
            connection.start();

            // Создал сессию
            Session session = connection.createSession(true);

            // Создал очередь
            Destination destination = session.createDestination("topic");

            // Создал продюсера
            Producer producer = session.createProducer(destination);

            // Завершение работы ПО
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    session.close();
                    connection.close();
                    System.out.println("Соединение и сессия были закрыты.");
                } catch (Exception e) {
                    System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
                }
            }));

            // Чтение и отправка сообщений
            while (true) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {

                        producer.send(line);
                        //System.out.println("Sent: " + line);


                        Thread.sleep(2000);
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при чтении файла: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
