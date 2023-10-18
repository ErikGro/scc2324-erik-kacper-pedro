package scc.srv;

import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.data.RentalDAO;
import scc.db.CosmosDBLayer;
import scc.utils.Constants;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Path("/rental")
public class RentalResource {
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(RentalDAO rentalDAO) {
        String id = UUID.randomUUID().toString();
        rentalDAO.setId(id);
        CosmosItemResponse<RentalDAO> response = CosmosDBLayer.getInstance().rentalDB.upsertRental(rentalDAO);

        if (response.getStatusCode() == 201) {
            try {
                URI rentalURL = new URI(Constants.getApplicationURL() + "/rest/rental/" + id);
                return Response.created(rentalURL).build();
            } catch (URISyntaxException e) {
                return Response.status(500).build();
            }
        }

        return Response.status(response.getStatusCode()).build();
    }

    /**
     * Update a rental by a given id
     * @param id the id of the rental to be updated
     * @param rentalDAO the updated content
     * @return nothing - 2xx if update was successful
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") String id, RentalDAO rentalDAO) {
        rentalDAO.setId(id);
        CosmosItemResponse<RentalDAO> response = CosmosDBLayer.getInstance().rentalDB.upsertRental(rentalDAO);

        return Response.status(response.getStatusCode()).build();
    }

    /**
     * If rental with given id exists, return rental as JSON
     * @param id of the rental
     * @return Response with rental JSON for given id in body
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRentalByID(@PathParam("id") String id) {
        CosmosItemResponse<RentalDAO> response = CosmosDBLayer.getInstance().rentalDB.getRentalByID(id);

        return Response.accepted(response.getItem()).build();
    }

    /**
     * Returns all rentals for a given query parameter userID
     * @param userID the user who is the tenant for the rentals
     * @return all rentals for a given query parameter userID
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
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
}
