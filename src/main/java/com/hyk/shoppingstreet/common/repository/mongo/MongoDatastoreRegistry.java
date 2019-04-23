package com.hyk.shoppingstreet.common.repository.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MongoDatastoreRegistry extends ApplicationObjectSupport {

    private Map<String, Datastore> datastoreMap;

    private Map<String, MongoClient> clientMap;

    @PostConstruct
    public void init() {
        Map<String, MongoConfig> beanMap = getApplicationContext().getBeansOfType(MongoConfig.class);

        clientMap = beanMap.values().stream().collect(Collectors.toMap(config -> config.getKey(),
            config -> {
                MongoCredential credential =
                    MongoCredential.createCredential(config.getUserName(), config.getDbName(),
                        config.getPassword().toCharArray());
                List<ServerAddress> serverAddressList = Stream.of(config.getHosts()).map(uri -> {
                    String[] toks = uri.split(":");
                    Assert.isTrue(toks.length > 0);
                    String host = toks[0];
                    int port = toks.length > 1 ? Integer.parseInt(toks[1]) : 27017;
                    return new ServerAddress(host, port);
                }).collect(Collectors.toList());

//                    MongoClient client = StringUtils.isNotBlank(config.getReplicaSet()) ?
//                            new MongoClient(serverAddressList, Arrays.asList(credential),
//                                    MongoClientOptions.builder().requiredReplicaSetName(config.getReplicaSet()).build())
//                            : new MongoClient(serverAddressList, Arrays.asList(credential));

                MongoClient client = new MongoClient(serverAddressList, Arrays.asList(credential));

                return client;
            }));

        datastoreMap = beanMap.values().stream().collect(Collectors.toMap(config -> config.getKey(),
            config -> {
                MongoClient client = clientMap.get(config.getKey());

                Morphia morphia = new Morphia();
                config.getMapPackages().forEach(
                    mapPackage -> morphia.mapPackage(mapPackage, true)
                );

                Datastore datastore = morphia.createDatastore(client, config.getDbName());
                datastore.ensureIndexes();
                return datastore;
            }));
    }

    public Datastore getDatastore(String key) {
        return datastoreMap.get(key);
    }

    public MongoClient getClient(String key) {
        return clientMap.get(key);
    }
}