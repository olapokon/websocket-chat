FROM rabbitmq:3.8-management

# https://www.rabbitmq.com/management.html
RUN rabbitmq-plugins enable --offline rabbitmq_management
# https://www.rabbitmq.com/stomp.html
RUN rabbitmq-plugins enable --offline rabbitmq_stomp
# https://www.rabbitmq.com/web-stomp.html
RUN rabbitmq-plugins enable --offline rabbitmq_web_stomp

EXPOSE 15671 15672 15674 61613
