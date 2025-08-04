package com.dinet;
import javax.annotation.PostConstruct;

import com.dinet.domain.model.Cliente;
import com.dinet.infrastructure.out.persistence.ClienteEntity;
import com.dinet.infrastructure.out.persistence.ClienteRepository;
import com.dinet.infrastructure.out.persistence.ZonaEntity;
import com.dinet.infrastructure.out.persistence.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MySpringBootApplication {

	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private ZonaRepository zonaRepository;

	public static void main(String[] args) {
		SpringApplication.run(MySpringBootApplication.class, args);
	}

	@PostConstruct
	private void initDb() {
		List<ClienteEntity> clientes = new ArrayList<>();
		ClienteEntity cliente1 = new ClienteEntity();
		cliente1.setId("CLI-123");
		cliente1.setNombre("Cliente 123");
		clientes.add(cliente1);

		ClienteEntity cliente2 = new ClienteEntity();
		cliente2.setId("CLI-999");
		cliente2.setNombre("Cliente 999");
		clientes.add(cliente2);

		clienteRepository.saveAll(clientes);

		List<ZonaEntity> zonas = new ArrayList<>();
		ZonaEntity zona1 = new ZonaEntity();
		zona1.setCodigo("ZONA1");
		zona1.setSoporteRefrigeracion(true);
		zonas.add(zona1);

		ZonaEntity zona2 = new ZonaEntity();
		zona2.setCodigo("ZONA5");
		zona2.setSoporteRefrigeracion(false);
		zonas.add(zona2);

		zonaRepository.saveAll(zonas);
	}
}
