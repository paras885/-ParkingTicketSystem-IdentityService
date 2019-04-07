package org.fsociety.identityservice.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@PropertySource(value = MongoDBConfiguration.MONGO_PROPERTIES_FILE_SOURCE)
public class MongoDBConfiguration {

    public final static String MONGO_CLIENT_BEAN_IDENTIFIER = "mongoClientBean";
    public final static String MONGO_DATABASE_BEAN_IDENTIFIER = "mongoDatabaseBean";
    public final static String MONGO_CODEC_REGISTRY_BEAN_IDENTIFIER = "mongoCodecRegistry";
    public final static String MONGO_PROPERTIES_FILE_SOURCE = "classpath:mongodb.properties";

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${cluster_url}")
    private String cluster_url;

    @Value("${database_name}")
    private String database_name;

    @Bean(name = MONGO_CLIENT_BEAN_IDENTIFIER)
    public MongoClient getMongoClient() {
        final String mongodbUrl = String.format("mongodb+srv://%s:%s@%s/test", username, password, cluster_url);
        return MongoClients.create(mongodbUrl);
    }

    @Bean(name = MONGO_CODEC_REGISTRY_BEAN_IDENTIFIER)
    public CodecRegistry getCodecRegistry() {
        return fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }

    @Bean(name = MONGO_DATABASE_BEAN_IDENTIFIER)
    public MongoDatabase getMongoDatabase() {
        return getMongoClient()
            .getDatabase(database_name)
            .withCodecRegistry(getCodecRegistry());
    }
}
