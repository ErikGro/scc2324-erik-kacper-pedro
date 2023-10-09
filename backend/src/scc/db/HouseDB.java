package scc.db;

import com.azure.cosmos.CosmosContainer;

public class HouseDB extends DBContainer {
    HouseDB(CosmosContainer container) {
        super(container);
    }
}
