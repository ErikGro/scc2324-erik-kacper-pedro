package pt.unl.fct.di.scc;

import com.azure.cosmos.*;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    CosmosContainer container;

    Function() {
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

        container = db.getContainer("houses");
    }

    @FunctionName("periodic-compute")
    public void updateDiscountedNearFuture(
            @TimerTrigger(name ="periodicSetTime", schedule = "0 * * * *")
                                String timerInfo,
                                ExecutionContext context) {
        CosmosPagedIterable<HouseDAO> houses = getDiscountedHousesNearFuture();
        

    }

    public CosmosPagedIterable<HouseDAO> getDiscountedHousesNearFuture() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startDate = dateFormat.format(cal.getTime());

        cal.add(Calendar.MONTH, 3);
        String endDate = dateFormat.format(cal.getTime());

        String query = "SELECT * FROM houses WHERE EXISTS (SELECT VALUE p FROM p IN houses.availablePeriods WHERE p.startDate >= \"" + startDate + "\" AND p.startDate <= \"" + endDate + "\" AND IS_DEFINED(p.promotionPrice))";

        return container.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);
    }
}
