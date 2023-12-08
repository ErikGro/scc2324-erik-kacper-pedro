    package pt.unl.fct.di.scc;


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
					 function.garbageCollector();
                    break;
                case "garbageCollector":
                    function.garbageCollector();
                    break;
                default:
                    System.out.println("No valid function name provided.");
            }
        }
       
        public void updateDiscountedNearFuture() {
           DB.getInstance().getDiscountedHousesNearFuture();
        }

       
        public void garbageCollector() {
            DB.getInstance().removeDeletedUserEntries();
        }
    }
