A chat application using websockets and a RabbitMQ message broker. Includes a very basic web UI.

<br>

To deploy locally:

1. Set the GITHUB_OAUTH2_CLIENT_ID and GITHUB_OAUTH2_CLIENT_SECRET environment variables.
2. Start the RabbitMQ broker using docker/rabbitmq/docker-compose.yml or set application.properties app.use-external-broker to false.
3. Package and run the java application.
