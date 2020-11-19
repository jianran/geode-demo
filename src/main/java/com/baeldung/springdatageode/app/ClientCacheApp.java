package com.baeldung.springdatageode.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableIndexing;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import com.baeldung.springdatageode.controller.AppController;
import com.baeldung.springdatageode.domain.Author;
import com.baeldung.springdatageode.repo.AuthorRepository;
import com.baeldung.springdatageode.service.AuthorService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.Random;

@SpringBootApplication
@ClientCacheApplication(subscriptionEnabled = true)
@EnableEntityDefinedRegions(basePackageClasses = Author.class)
@EnableIndexing
@EnableGemfireRepositories(basePackageClasses = AuthorRepository.class)
@ComponentScan(basePackageClasses = {AppController.class, AuthorService.class, Stater.class})
@EnableClusterConfiguration(useHttp = true, requireHttps=false)
@EnableContinuousQueries
public class ClientCacheApp {
    
    public static void main(String[] args) {
        SpringApplication.run(ClientCacheApp.class, args);
    }
    
}

@Component
class Stater implements ApplicationListener<ApplicationReadyEvent> {

    private final AuthorRepository authorRepository;

    private static final String[] FIRST_NAME_DIC = "toarey peter fanhua simba java theia keke jeff tom".split(" ");

    private static final String[] LAST_NAME_DIC = "lee tan long smos lee ma pence cox trumo".split(" ");


    Stater(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.authorRepository.findAll().forEach(this.authorRepository::delete);
        int size = new Random().nextInt(1000);
        for (int i = 0; i < size + 100; i ++) {
            Author author = new Author();
            author.setId((long) i);
            author.setAge(new Random().nextInt(100));
            int nameIndex = new Random().nextInt(FIRST_NAME_DIC.length);
            author.setFirstName(FIRST_NAME_DIC[nameIndex]);
            author.setLastName(LAST_NAME_DIC[nameIndex]);
            Author saved = this.authorRepository.save(author);
            Optional<Author> found = this.authorRepository.findById(saved.getId());
            Assert.notNull(found, "need found in database");
        }
    }
}
