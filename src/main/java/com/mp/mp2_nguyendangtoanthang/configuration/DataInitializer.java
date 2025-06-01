package com.mp.mp2_nguyendangtoanthang.configuration;

import com.mp.mp2_nguyendangtoanthang.entity.Receptionist;
import com.mp.mp2_nguyendangtoanthang.repository.ReceptionistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ReceptionistRepository receptionistRepository) {
        return args -> {
            if (receptionistRepository.count() == 0) {
                Receptionist defaultReceptionist = new Receptionist();
                defaultReceptionist.setName("NguyenDangToanThang");
                defaultReceptionist.setPassword("123456");

                receptionistRepository.save(defaultReceptionist);
            }
        };
    }
}
