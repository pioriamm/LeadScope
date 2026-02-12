package br.com.pioriam.leadscope.teste;

import br.com.pioriam.leadscope.modelos.Cliente;
import br.com.pioriam.leadscope.repositorio.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Teste implements CommandLineRunner {

    @Autowired
    private ClienteRepository repository;

    @Override
    public void run(String... args) {
        Cliente c = new Cliente();
        c.setNome("Marcelo");
        repository.save(c);
        System.out.println("Salvo ID: " + c.getId());
    }
}
