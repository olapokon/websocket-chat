package olapokon.websocketchat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
public class ApplicationProperties {

    /**
     * If true, use external RabbitMQ broker, otherwise use Spring's simple broker.
     */
    private Boolean useExternalBroker = true;

    /**
     * The websocket handshake endpoint.
     */
    private String websocketUrl = "";

    public Boolean getUseExternalBroker() {
        return useExternalBroker;
    }

    public void setUseExternalBroker(Boolean useExternalBroker) {
        this.useExternalBroker = useExternalBroker;
    }

    public String getWebsocketUrl() {
        return websocketUrl;
    }

    public void setWebsocketUrl(String websocketUrl) {
        this.websocketUrl = websocketUrl;
    }
}
