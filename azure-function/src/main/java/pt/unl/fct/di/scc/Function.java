    package pt.unl.fct.di.scc;

    import com.azure.cosmos.util.CosmosPagedIterable;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import redis.clients.jedis.Jedis;
    import java.util.Set;
    import java.util.stream.Collectors;

    /**
     * Azure Functions with HTTP Trigger.
     */
    public class Function {
        public static void main(String[] args) {
            Function function = new Function();
            
            // Assuming args[0] contains the function name to execute
            String functionName = args.length > 0 ? args[0] : "";
    
            switch (functionName) {
                case "updateDiscountedNearFuture":
                    function.updateDiscountedNearFuture();
                    break;
                case "garbageCollector":
                    function.garbageCollector();
                    break;
                default:
                    System.out.println("No valid function name provided.");
            }
        }
       
        public void updateDiscountedNearFuture() {
            CosmosPagedIterable<HouseDAO> houses = DB.getInstance().getDiscountedHousesNearFuture();

            Set<HouseDAO> set = houses.stream().collect(Collectors.toSet());

            try (Jedis jedis = RedisCache.getCachePool().getResource()) {
                ObjectMapper mapper = new ObjectMapper();
                jedis.set("discountedNearFuture", mapper.writeValueAsString(set));
            } catch (Exception ignored) {
                // Do nothing
            }
        }

       
        public void garbageCollector() {
            DB.getInstance().removeDeletedUserEntries();
        }
    }
