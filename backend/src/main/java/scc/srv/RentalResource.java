package scc.srv;

import com.azure.cosmos.models.CosmosItemResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.data.Rental;
import scc.data.RentalDAO;
import scc.db.CosmosDBLayer;

import java.util.UUID;

@Path("/rental")

public class RentalResource {
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(Rental rental) {
        RentalDAO rentalDAO = new RentalDAO(rental);
        rentalDAO.setId(UUID.randomUUID().toString());
        CosmosItemResponse<RentalDAO> response = CosmosDBLayer.getInstance().rentalDB.putRental(rentalDAO);

        return Response.status(response.getStatusCode()).build();
    }
}
