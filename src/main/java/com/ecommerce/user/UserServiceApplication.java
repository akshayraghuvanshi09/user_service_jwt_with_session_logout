package com.ecommerce.user;

import com.ecommerce.user.entity.UserRole;
import com.ecommerce.user.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		UserRole role = new UserRole();
		role.setRid(101);
		role.setRname("CUSTOMER");

		UserRole role2 = new UserRole();
		role2.setRid(102);
		role2.setRname("ADMIN");

		roleRepository.save(role);
		roleRepository.save(role2);
		
	}

}
