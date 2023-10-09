package scc.db;

import com.azure.cosmos.CosmosContainer;

abstract class DBContainer {
    protected CosmosContainer container;

    DBContainer(CosmosContainer container) {
        this.container = container;
    }
}
