package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class S05T01N01SedoBulpeJordiApplication_F2 {

	public static void main(String[] args) {
		SpringApplication.run(S05T01N01SedoBulpeJordiApplication_F2.class, args);
	}

}
