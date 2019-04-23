package com.hyk.shoppingstreet.common.repository.mongo;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MongoConfig {
    @Builder.Default
    private String key = "default";
    private String dbName;
    private String password;
    private String userName;

    private String[] hosts;
    private String replicaSet;

    private List<String> mapPackages;
}
