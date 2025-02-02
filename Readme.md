# *job4j_cinema*

job4j_cinema - веб-приложение по покупке билетов в кинотеатр. Релизован на языке Java с использованием фреймворка 
Spring (Boot + MVC).

### Основной функционал приложения:
- Вывод киносеансов и фильмов
- Покупка билетов на фильм
- Регистрация и вход


  **Новигационная панель состоит из:**
1) Расписания сеансов кинотеатра. Отображается список всех сеансов. При выборе определенного сеанса, пользователь переходит на страницу покупки билета,
   где отображена информация о фильме, его постер, дата начала и выбор места на билет. 
   Билеты могут покупать только зарегистрированные пользователи;
2) Кинотеки, со списком всех фильмов кинотеатра;
3) Вкладка регистрации/входа, в которой пользователь может авторизоваться в системе или зарегистрироваться

**Стек проекта:**
- **Язык**: Java 17
- **Сборка проекта**: Maven 3.9.6
- **Фреймворк**: Spring Boot
- **База данных**: PROD - PostgreSQL 16.2 / TEST - H2 Database
- **Миграции БД**: Liquibase
- **ORM**: Sql2o
- **Шаблоны представлений**: Thymeleaf
- **Фронтенд**: Bootstrap

**Требования к окружению**: Java 17, Maven 3.8 или выше, PostgreSQL 14 или выше.

Если у Вас есть вопросы или предложения, вы можете связаться со мной по электронной почте: **loganxmen97@gmail.com**