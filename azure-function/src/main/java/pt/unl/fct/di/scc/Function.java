package pt.unl.fct.di.scc;

import com.azure.cosmos.util.CosmosPagedIterable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;
import redis.clients.jedis.Jedis;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    @FunctionName("periodic-compute")
    public void updateDiscountedNearFuture(@TimerTrigger(name = "periodicSetTime", schedule = "0 * * * *")
                                           String timerInfo,
                                           ExecutionContext context) {
        CosmosPagedIterable<HouseDAO> houses = DB.getInstance().getDiscountedHousesNearFuture();

        Set<HouseDAO> set = houses.stream().collect(Collectors.toSet());

        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            ObjectMapper mapper = new ObjectMapper();
            jedis.set("discountedNearFuture", mapper.writeValueAsString(set));
        } catch (Exception ignored) {
            // Do nothing
        }
    }

    @FunctionName("periodic-compute")
    public void garbageCollector(@TimerTrigger(name = "periodicSetTime", schedule = "0 */6 * * *")
                                           String timerInfo,
                                           ExecutionContext context) {
        DB.getInstance().removeDeletedUserEntries();
    }
}
