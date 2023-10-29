package scc.srv;

import com.azure.cosmos.models.CosmosItemResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.cache.HouseService;
import scc.cache.RentalService;
import scc.cache.ServiceResponse;
import scc.data.RentalDAO;
import scc.data.house.AvailablePeriod;
import scc.data.house.HouseDAO;
import scc.utils.Constants;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Resource for accessing rentals
 */
@Path("/house/{houseID}/rental")
public class RentalResource {
    private final HouseService houseService = new HouseService();
    private final RentalService rentalService = new RentalService();

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRental(@PathParam("houseID") String houseID, RentalDAO rentalDAO) {
        String rentalID = UUID.randomUUID().toString();
        rentalDAO.setId(rentalID);
        rentalDAO.setHouseID(houseID);

        Optional<HouseDAO> house = houseService.getByID(houseID).getItem();
        if (house.isEmpty()) {
            return Response.noContent().build();
        }

        // TODO catch Exception
        LocalDate start = LocalDate.parse(rentalDAO.getStartDate(), Constants.dateFormat);
        LocalDate end = LocalDate.parse(rentalDAO.getEndDate(), Constants.dateFormat);

        // Check if there is an available period which contains the wanted rental period
        Optional<AvailablePeriod> period = house.get()
                .getAvailablePeriods()
                .stream()
                .filter(p -> p.containsPeriod(start, end))
                .findFirst();

        if (period.isEmpty()) {
            return Response.noContent().build();
        }

        // Update house available periods
        Set<AvailablePeriod> newPeriods = period.get().subtract(start, end);
        house.get().getAvailablePeriods().remove(period.get());
        house.get().getAvailablePeriods().addAll(newPeriods);
        houseService.upsert(house.get());

        // Compute price of the rental
        long daysBetween = start.until(end, ChronoUnit.DAYS);
        Float price = daysBetween * period.get().getNormalPricePerDay();
        rentalDAO.setPrice(price);
        ServiceResponse<RentalDAO> response = rentalService.upsert(rentalDAO);

        if (response.getStatusCode() == 201) {
            try {
                String path = "/rest/house/" + houseID + "/rental/" + rentalID;
                URI rentalURL = new URI(Constants.getApplicationURL() + path);
                return Response.created(rentalURL).build();
            } catch (URISyntaxException e) {
                return Response.status(500).build();
            }
        }

        return Response.status(response.getStatusCode()).build();
    }

    /**
     * Update a rental by a given id
     * @param houseID the id of the house to which the rental belongs
     * @param rentalID the id of the rental to be updated
     * @param rentalDAO the updated content
     * @return nothing - 2xx if update was successful
     */
    @PUT
    @Path("/{rentalID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putRental(@PathParam("houseID") String houseID, @PathParam("rentalID") String rentalID, RentalDAO rentalDAO) {
        rentalDAO.setId(rentalID);
        rentalDAO.setHouseID(houseID);
        ServiceResponse<RentalDAO> response = rentalService.upsert(rentalDAO);

        return Response.status(response.getStatusCode()).build();
    }

    /**
     * If rental with given id exists, return rental as JSON
     * @param houseID the id of the house to which the rental belongs
     * @param rentalID the id of the rental to be fetched
     * @return Response with rental JSON for given id in body
     */
    @GET
    @Path("/{rentalID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRentalByID(@PathParam("houseID") String houseID, @PathParam("rentalID") String rentalID) {
        ServiceResponse<RentalDAO> response = rentalService.getByID(rentalID);

        return Response.accepted(response.getItem()).build();
    }

    /**
     * Delete a rental by a given id
     * @param rentalID of the rental to be deleted
     * @return nothing - 2xx if delete succeeded
     */
    @DELETE
    @Path("/{rentalID}")
    public Response deleteRental(@PathParam("houseID") String houseID, @PathParam("rentalID") String rentalID) {
        ServiceResponse<RentalDAO> response = rentalService.deleteByID(rentalID);

        return Response.status(response.getStatusCode()).build();
    }
}
