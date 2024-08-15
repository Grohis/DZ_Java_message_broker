package ru.pflb.class5;
import ru.pflb.mq.dummy.implementation.*;
import ru.pflb.mq.dummy.interfaces.*;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Создал и запустил соединение
            Connection connection = new ConnectionImpl();
            connection.start();

            // Создал сессию
            Session session = connection.createSession(true);

            // Создал очередь
            Destination destination = session.createDestination("topic");

            // Создал продюсера
            Producer producer = session.createProducer(destination);

            // Спислк сообщений
            List<String> messages = Arrays.asList("Четыре", "Пять", "Шесть");
            //Итерация по списку с задержкой
            for (String message : messages) {
                producer.send(message); // отправляет сообщения в консоль
                Thread.sleep(2000); // задержка (2 сек)
            }

            // Закрыл сессию и соединение
            session.close();
            connection.close();

            // Исключение...
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }

    }
}