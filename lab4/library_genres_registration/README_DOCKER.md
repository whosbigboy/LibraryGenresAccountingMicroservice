# Docker-файлы для микросервиса учета жанров книг

## Файлы проекта

В проекте созданы два файла для контейнеризации:

1. **Dockerfile** - многоэтапный файл сборки образа
2. **docker-compose.yml** - конфигурация для запуска сервиса

## Как запустить микросервис

### Вариант 1: Использование Docker Compose (рекомендуется)

```bash
# Перейдите в директорию проекта
cd /workspace/lab4/library_genres_registration

# Соберите и запустите сервис
docker-compose up --build

# Или в фоновом режиме
docker-compose up -d --build
```

### Вариант 2: Использование только Docker

```bash
# Перейдите в директорию проекта
cd /workspace/lab4/library_genres_registration

# Соберите Docker-образ
docker build -t library-genres-service .

# Запустите контейнер
docker run -d -p 8080:8080 --name library-genres-service library-genres-service
```

## Проверка работы

После запуска сервис будет доступен по адресу: `http://localhost:8080`

Проверьте работу API:

```bash
# Получить все жанры
curl http://localhost:8080/genres

# Получить жанр по ID
curl http://localhost:8080/genres/1

# Создать новый жанр
curl -X POST http://localhost:8080/genres \
  -H "Content-Type: application/json" \
  -d '{"name": "Фантастика", "description": "Научная фантастика и фэнтези"}'

# Обновить жанр
curl -X PUT http://localhost:8080/genres/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Обновлённая фантастика", "description": "Обновлённое описание"}'

# Удалить жанр
curl -X DELETE http://localhost:8080/genres/1
```

## Остановка сервиса

```bash
# Для Docker Compose
docker-compose down

# Для Docker
docker stop library-genres-service
docker rm library-genres-service
```

## Примечания

- Сервис работает на порту 8080
- Данные хранятся в памяти (ConcurrentHashMap) и будут потеряны при перезапуске контейнера
- Для учебного проекта это допустимо, так как нет реальной базы данных
