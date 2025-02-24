# 🎮 Проект по автоматизации тестовых сценариев (API)

<p align="center">
  <!-- Замените путь и название файла на свою картинку, если нужно -->
  <img width="30%" title="API" src="images/api-logo.png">
</p>

## 🧑‍💻 Запуск тестовых сценариев

### Автоматизированные тесты

Данный проект покрывает базовые сценарии тестирования API. Тесты сгруппированы по сущностям:

- **Продукты** 
    - [x] Получение всех продуктов (GET)
    - [x] Создание продукта (POST)
    - [x] Обновление информации о продукте (POST)
    - [x] Удаление товара (DELETE)
    - [x] Сортировка товаров (GET)

---

# 💻 Используемые инструменты

<p align="center">
 
  <code><img width="5%" title="Java" src="images/java-logo.svg"></code>
  <code><img width="5%" title="JUnit5" src="images/junit5-logo.svg"></code>
  <code><img width="5%" title="Gradle" src="images/gradle-logo.svg"></code>
  <code><img width="5%" title="OkHttp" src="images/okhttp-logo.png"></code>
  <!-- Или RestAssured -->
  <code><img width="5%" title="Allure Report" src="images/allure-Report-logo.svg"></code>
  <code><img width="5%" title="Allure TestOps" src="images/allure-ee-logo.svg"></code>
  <code><img width="5%" title="GitHub" src="images/git-logo.svg"></code>
  <code><img width="5%" title="Jenkins" src="images/jenkins-logo.svg"></code>
  <code><img width="5%" title="Telegram" src="images/Telegram.svg"></code>
</p>

+ **Java** – основной язык программирования проекта.
+ **OkHttp** *(или RestAssured)* – библиотека (фреймворк) для отправки HTTP-запросов и проверки ответов.
+ **JUnit 5** – фреймворк для написания и запуска тестов.
+ **Gradle** – система сборки и управления зависимостями.
+ **Jenkins** – CI/CD-сервер для автоматического запуска тестов.
+ **Allure Report** – инструмент для наглядного отображения результатов тестирования.
+ **Telegram API** – для отправки уведомлений о результатах теста (опционально).

---

# 🚀 Как запустить тесты

### Локальный запуск через командную строку

> **Важно**: Параметры (например, базовый URL) могут быть заданы в коде или в файлах настроек.

Для локального запуска тестов:
```bash
gradle jenkins_test
