A chat application using websockets and a RabbitMQ message broker. Includes a very basic web UI.

### live at https://infinite-beach-26862.herokuapp.com/

If no HTTP requests are made for some time, Heroku will go into sleep mode due to inactivity. In that case, a chatroom will become unresponsive and will not be able to reestablish the socket connection, unless the page is refreshed to cause Heroku to come out of sleep mode, which takes several seconds.

The first time the app is accessed can also take several seconds, for the same reason.

<br>

To deploy locally:

1. Set the GITHUB_OAUTH2_CLIENT_ID and GITHUB_OAUTH2_CLIENT_SECRET environment variables.
2. Start the RabbitMQ broker using docker/rabbitmq/docker-compose.yml or set application.properties app.use-external-broker to false.
3. Package and run the java application.