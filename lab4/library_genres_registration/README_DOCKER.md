Использование Docker Compose

```bash
# Перейдите в директорию проекта
cd /workspace/lab4/library_genres_registration

# Соберите и запустите сервис
docker-compose up --build

# Или
docker-compose up -d --build
```

### Вариант 2: Использование только Docker

```bash
cd /workspace/lab4/library_genres_registration

docker build -t library-genres-service .

docker run -d -p 8080:8080 --name library-genres-service library-genres-service
```
