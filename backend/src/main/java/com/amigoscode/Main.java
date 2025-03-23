package com.amigoscode;

import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.amigoscode.customer.Gender;
import com.amigoscode.s3.S3Buckets;
import com.amigoscode.s3.S3Service;
import com.github.javafaker.Faker;
@SpringBootApplication(scanBasePackages = "com.amigoscode")


public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            S3Service s3Service,
            S3Buckets s3Buckets) {
        return args -> {
            createRandomCustomers(customerRepository, passwordEncoder);
            //testBucketUploadAndDownload(s3Service, s3Buckets);


        };
    }

    private static void testBucketUploadAndDownload(S3Service s3Service, S3Buckets s3Buckets) {
            try {
                s3Service.putObject(s3Buckets.getCustomer(), "foo", "Hello world".getBytes());
                byte[] obj = s3Service.getObject(s3Buckets.getCustomer(), "foo");
                System.out.println("Horray: " + new String(obj));
            } catch (Exception e) {
                System.err.println("Error interacting with S3: " + e.getMessage());
            }
    
            // Crear clientes aleatorios
            //createRandomCustomers(customerRepository, passwordEncoder);

    }
    
    private static void createRandomCustomers(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        var faker = new Faker();
        Random random = new Random();
        var name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        int age = random.nextInt(16, 99);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@amigoscode.com";
        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                passwordEncoder.encode("password"),
                age,
                gender);
        customerRepository.save(customer);
        System.out.println("Saved customer: " + customer);
    }

}
