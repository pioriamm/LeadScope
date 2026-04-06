package br.com.pioriam.leadscope.configs;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI leadscopeOpenAPI() {

        Server prodServer = new Server();
        prodServer.setUrl("https://leadscope-vaoo.onrender.com");
        prodServer.setDescription("Ambiente de Produção");

        Contact contato = new Contact();
        contato.setName("Conciliadora");
        contato.setEmail("jhonny@conciliadora.com.br");
        contato.setUrl("https://conciliadora.com.br");

        License licenca = new License().name("MIT License").url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("LeadScope API")
                .version("v1.0.0")
                .description("""
                    <img src="/logo.png" width="60"/>
                   
                    API responsável pela prospecção de empresas via CNPJ.
                    
                    **Funcionalidades principais**
                    
                    - Consulta de empresas via CNPJ
                    - Identificação de sócios
                    - Descoberta de empresas relacionadas
                    - Armazenamento de base prospectada
                    
                    
                    
                    """)
                .contact(contato)
                .license(licenca);

        return new OpenAPI()
                .info(info)
                .servers(List.of(prodServer))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório do projeto")
                        .url("https://github.com"));
    }
}