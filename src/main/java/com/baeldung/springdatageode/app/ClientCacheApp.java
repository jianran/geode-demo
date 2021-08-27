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

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootApplication
@ClientCacheApplication(subscriptionEnabled = true)
@EnableEntityDefinedRegions(basePackageClasses = Author.class)
@EnableIndexing
@EnableGemfireRepositories(basePackageClasses = AuthorRepository.class)
@ComponentScan(basePackageClasses = {AppController.class, AuthorService.class, Stater.class})
@EnableClusterConfiguration
@EnableContinuousQueries
public class ClientCacheApp {
    
    public static void main(String[] args) {
        SpringApplication.run(ClientCacheApp.class, args);
    }
    
    @Bean
    public ClientCache commonGeodeCache(String locatorHosts, int minConnection, int maxConnection, int timeout) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ClientCacheFactory clientCacheFactory = new ClientCacheFactory();
        if (locatorHosts != null) {
            String[] locatorHostArray = locatorHosts.split(",");
            if (locatorHostArray != null) {
                for (String locator : locatorHostArray) {
                    String host = locator.substring(0, locator.indexOf("["));
                    Integer port = Integer.valueOf(locator.substring(locator.indexOf("[") + 1, locator.indexOf("]")));
                    clientCacheFactory = clientCacheFactory.addPoolLocator(host, port);
                }
            }
            if (timeout > 0) {
                clientCacheFactory.setPoolReadTimeout(timeout);
                clientCacheFactory.setPoolSocketConnectTimeout(timeout);
            }
            if (minConnection > 0) {
                clientCacheFactory.setPoolMinConnections(minConnection);
            }
            if (maxConnection > 0) {
                clientCacheFactory.setPoolMaxConnections(maxConnection);
            }
            clientCacheFactory.setPoolFreeConnectionTimeout(50);
            clientCacheFactory.setPoolRetryAttempts(1);
            clientCacheFactory.setPoolSubscriptionRedundancy(2);
            clientCacheFactory.setPoolSubscriptionMessageTrackingTimeout(10000);
            clientCacheFactory.setPoolPingInterval(60 * 1000);
            clientCacheFactory.setPoolSubscriptionEnabled(true);
            clientCacheFactory.setPoolSubscriptionAckInterval(60 * 1000);
            clientCacheFactory.setPoolSocketBufferSize(1024 * 1024 * 8);
            clientCacheFactory.setPdxSerializer(new ReflectionBasedAutoSerializer("com.*"));
            return clientCacheFactory.create();
        }
    }
    
}

@Component
class Stater implements ApplicationListener<ApplicationReadyEvent> {

    private final AuthorRepository authorRepository;
    private final AverageAgeFunctionInvoker invoker;


    private static final String[] FIRST_NAME_DIC = "toarey peter fanhua simba java theia keke jeff tom".split(" ");

    private static final String[] LAST_NAME_DIC = "lee tan long smos lee ma pence cox trumo".split(" ");


    Stater(AuthorRepository authorRepository, AverageAgeFunctionInvoker invoker) {
        this.authorRepository = authorRepository;
        this.invoker = invoker;
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

        Collection<Double> averages = this.invoker.average();
        Double clusterAvergage = averages
                .stream()
                .collect(Collectors.averagingDouble(x -> x));

        System.out.println("cluster average: " + clusterAvergage);
    }
}
