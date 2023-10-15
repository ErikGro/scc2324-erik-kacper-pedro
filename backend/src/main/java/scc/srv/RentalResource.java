package scc.srv;

import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;
import jakarta.ws.rs.*;
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
        int statusCode = putRental(UUID.randomUUID().toString(), rental);

        return Response.status(statusCode).build();
    }

    /**
     * Update a rental by a given id
     * @param id the id of the rental to be updated
     * @param rental the updated content
     * @return nothing - 2xx if update was successful
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") String id, Rental rental) {
        int statusCode = putRental(id, rental);

        return Response.status(statusCode).build();
    }

    /**
     * If rental with given id exists, return rental as JSON
     * @param id
     * @return Response with rental JSON for given id in body
     */
    @GET
    @Path("/{id}")
    public Response getRentalByID(@PathParam("id") String id) {
        CosmosItemResponse<RentalDAO> response = CosmosDBLayer.getInstance().rentalDB.getRentalByID(id);

        return Response.accepted(response.getItem()).build();
    }

    /**
     * Returns all rentals for a given query parameter userID
     * @param userID
     * @return all rentals for a given query parameter userID
     */
    @GET
    @Path("/{id}")
    public Response getRentalsByUserID(@QueryParam("userID") String userID) {
        CosmosPagedIterable<RentalDAO> response = CosmosDBLayer.getInstance().rentalDB.getRentalsByUserID(userID);

        return Response.accepted(response.stream().toList()).build();
    }

    /**
     * Delete a rental by a given id
     * @param id of the rental to be deleted
     * @return nothing - 2xx if delete succeeded
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        CosmosItemResponse<Object> response = CosmosDBLayer.getInstance().rentalDB.deleteRental(id);
        return Response.status(response.getStatusCode()).build();
    }

    private int putRental(String id, Rental rental) {
        RentalDAO rentalDAO = new RentalDAO(rental);
        rentalDAO.setId(id);
        CosmosItemResponse<RentalDAO> response = CosmosDBLayer.getInstance().rentalDB.putRental(rentalDAO);

        return response.getStatusCode();
    }
}
