package br.com.pioriam.leadscope.configs;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class SwaggerAutoOpen {

    @EventListener(ApplicationReadyEvent.class)
    public void openSwagger() {

        String url = "http://localhost:8080/leads-doc";

        try {

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                return;
            }

            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + url);
            } else if (os.contains("win")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                Runtime.getRuntime().exec("xdg-open " + url);
            }

        } catch (Exception e) {
            System.out.println("Não foi possível abrir o navegador automaticamente");
        }
    }
}