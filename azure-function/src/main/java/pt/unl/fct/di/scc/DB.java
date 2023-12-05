package pt.unl.fct.di.scc;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClients;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import redis.clients.jedis.Jedis;

import static dev.morphia.query.filters.Filters.elemMatch;
import static dev.morphia.query.filters.Filters.gte;
import static dev.morphia.query.filters.Filters.lte;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DB {
    private static DB instance;
    private final MongoHouseCollection houseContainer;
    private final MongoRentalCollection rentalContainer;
    final Datastore datastore;
    public static synchronized DB getInstance() {
        if (instance != null)
            return instance;

        instance = new DB();

        return instance;
    }

    public DB() {
        this.datastore = Morphia.createDatastore(MongoClients.create(Constants.getMongoDBConnectionString()));

        houseContainer = new MongoHouseCollection(this.datastore);
        rentalContainer = new MongoRentalCollection(this.datastore);

    }

    
    synchronized public void getDiscountedHousesNearFuture() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startDate = dateFormat.format(cal.getTime());

        cal.add(Calendar.MONTH, 3);
        String endDate = dateFormat.format(cal.getTime());

        List<HouseDAO> houses = datastore.find(HouseDAO.class)
                .filter(elemMatch("available_periods", gte("start_date", startDate), lte("start_date", endDate)))
                .stream()
                .collect(Collectors.toList());
              Set<HouseDAO> set= houses.stream().collect(Collectors.toSet());
        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
                    ObjectMapper mapper = new ObjectMapper();
                    jedis.set("discountedNearFuture", mapper.writeValueAsString(set));
                } catch (Exception ignored) {
                    // Do nothing
                }
            
    }

    public void removeDeletedUserEntries() {
        String queryHouses = "SELECT * FROM houses WHERE houses.ownerID=\"DeletedUser\"";
        datastore.delete(queryHouses);
      /*  CosmosPagedIterable<Identifiable> responseHouses = houses.queryItems(queryHouses,
                new CosmosQueryRequestOptions(),
                Identifiable.class);
        responseHouses.forEach(o -> houses.deleteItem(o.getId(),
                new PartitionKey(o.getId()),
                new CosmosItemRequestOptions()));
        */
        String queryRental = "SELECT * FROM rental WHERE rental.ownerID=\"DeletedUser\"";
        datastore.delete(queryRental);
        /*CosmosPagedIterable<Identifiable> responseRentals = rentals.queryItems(queryRental,
                new CosmosQueryRequestOptions(),
                Identifiable.class);
        responseRentals.forEach(o -> rentals.deleteItem(o.getId(),
                new PartitionKey(o.getId()),
                new CosmosItemRequestOptions()));*/
    }
}
