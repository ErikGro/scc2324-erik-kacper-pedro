package pt.unl.fct.di.scc;

import com.azure.cosmos.*;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DB {
    private static DB instance;
    private final CosmosContainer houses;
    private final CosmosContainer rentals;

    public static synchronized DB getInstance() {
        if (instance != null)
            return instance;

        instance = new DB();

        return instance;
    }

    public DB() {
        CosmosClient client = new CosmosClientBuilder()
                .endpoint(Constants.getDBConnectionURL())
                .key(Constants.getDBKey())
                //.directMode()
                .gatewayMode()
                // replace by .directMode() for better performance
                .consistencyLevel(ConsistencyLevel.SESSION)
                .connectionSharingAcrossClientsEnabled(true)
                .contentResponseOnWriteEnabled(true)
                .buildClient();

        CosmosDatabase db = client.getDatabase(Constants.getDBName());

        houses = db.getContainer("houses");
        rentals = db.getContainer("rental");
    }

    public synchronized CosmosPagedIterable<HouseDAO> getDiscountedHousesNearFuture() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startDate = dateFormat.format(cal.getTime());

        cal.add(Calendar.MONTH, 3);
        String endDate = dateFormat.format(cal.getTime());

        String query = "SELECT * FROM houses WHERE EXISTS (SELECT VALUE p FROM p IN houses.availablePeriods WHERE p.startDate >= \"" + startDate + "\" AND p.startDate <= \"" + endDate + "\" AND IS_DEFINED(p.promotionPrice))";

        return houses.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);
    }

    public void removeDeletedUserEntries() {
        String queryHouses = "SELECT * FROM houses WHERE houses.ownerID=\"DeletedUser\"";
        CosmosPagedIterable<Identifiable> responseHouses = houses.queryItems(queryHouses,
                new CosmosQueryRequestOptions(),
                Identifiable.class);
        responseHouses.forEach(o -> houses.deleteItem(o.getId(),
                new PartitionKey(o.getId()),
                new CosmosItemRequestOptions()));

        String queryRental = "SELECT * FROM rental WHERE rental.ownerID=\"DeletedUser\"";
        CosmosPagedIterable<Identifiable> responseRentals = rentals.queryItems(queryRental,
                new CosmosQueryRequestOptions(),
                Identifiable.class);
        responseRentals.forEach(o -> rentals.deleteItem(o.getId(),
                new PartitionKey(o.getId()),
                new CosmosItemRequestOptions()));
    }
}
