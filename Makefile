.PHONY: start clean start-follow

start:
	docker compose up -d --build
start-follow:
	docker compose up --build
clean:
	docker compose down -v