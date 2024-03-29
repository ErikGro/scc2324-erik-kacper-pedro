package scc.srv;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.cache.HouseService;
import scc.cache.RentalService;
import scc.cache.ServiceResponse;
import scc.cache.UserService;
import scc.data.Rental;
import scc.data.RentalDAO;
import scc.data.house.AvailablePeriod;
import scc.data.house.HouseDAO;
import scc.utils.Constants;

import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.List;

/**
 * Resource for accessing rentals
 */
@Path("/house/{houseID}/rental")
public class RentalResource {
    private final HouseService houseService = new HouseService();
    private final RentalService rentalService = new RentalService();
    private final UserService userService = new UserService();

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRental(@CookieParam("scc:session") Cookie session,
                               @PathParam("houseID") String houseID,
                               RentalDAO rentalDAO) {
        String rentalID = UUID.randomUUID().toString();
        rentalDAO.setId(rentalID);
        rentalDAO.setHouseID(houseID);

        Optional<String> userID = userService.getUserIDBySession(session.getValue());
        if (userID.isEmpty())
            return Response.status(401).build();
        rentalDAO.setTenantID(userID.get());

        Optional<HouseDAO> optionalHouse = houseService.getByID(houseID).getItem();
        if (optionalHouse.isEmpty()) {
            throw new NotFoundException("House with the given id does not exist");
        }

        HouseDAO house = optionalHouse.get();

        if (rentalDAO.getStartDate() == null || rentalDAO.getEndDate() == null) {
            throw new BadRequestException("startDate and endDate mandatory");
        }

        LocalDate start = LocalDate.parse(rentalDAO.getStartDate(), Constants.dateFormat);
        LocalDate end = LocalDate.parse(rentalDAO.getEndDate(), Constants.dateFormat);

        // Check if there is an available period which contains the wanted rental period
        Optional<AvailablePeriod> optionalPeriod = house
                .getAvailablePeriods()
                .stream()
                .filter(p -> p.containsPeriod(start, end))
                .findFirst();

        if (optionalPeriod.isEmpty() || start.isEqual(end)) {
            throw new BadRequestException("There is no available period for the given period.");
        }

        AvailablePeriod period = optionalPeriod.get();

        // Update house available periods
        Set<AvailablePeriod> newPeriods = new HashSet<>(house.getAvailablePeriods());
        newPeriods.remove(period);
        newPeriods.addAll(period.subtract(start, end));
        house.setAvailablePeriods(newPeriods);
        houseService.upsert(house);

        // Compute price of the rental
        long daysBetween = start.until(end, ChronoUnit.DAYS);
        Float price = daysBetween * period.getNormalPricePerDay();
        rentalDAO.setPrice(price);
        ServiceResponse<RentalDAO> response = rentalService.upsert(rentalDAO);

        if (response.getStatusCode() != 201)
            return Response.status(response.getStatusCode()).build();

        URI rentalPath = URI.create("/house/" + houseID + "/rental/" + rentalID);
        return Response.created(rentalPath).build();
    }

    /**
     * Update a rental by a given id
     *
     * @param houseID   the id of the house to which the rental belongs
     * @param rentalID  the id of the rental to be updated
     * @param rentalDAO the updated content
     * @return nothing - 2xx if update was successful
     */
    @PUT
    @Path("/{rentalID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putRental(@CookieParam("scc:session") Cookie session,
                              @PathParam("houseID") String houseID,
                              @PathParam("rentalID") String rentalID,
                              RentalDAO rentalDAO) {
        ServiceResponse<RentalDAO> rentalResponse = rentalService.getByID(rentalID);

        if (rentalResponse.getItem().isEmpty())
            return Response.status(404).build();

        if (session == null || session.getValue() == null ||
                userService.userSessionInvalid(session.getValue(), rentalResponse.getItem().get().getTenantID()))
            return Response.status(401).build();

        rentalDAO.setId(rentalID);
        rentalDAO.setHouseID(houseID);
        rentalDAO.setTenantID(rentalResponse.getItem().get().getTenantID());
        ServiceResponse<RentalDAO> response = rentalService.upsert(rentalDAO);

        return Response.status(response.getStatusCode()).build();
    }

    /**
     * Returns a list of all rentals for a given house
     *
     * @param houseID  the id of the house to which the rental belongs
     * @return Response json array containing all rentals
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRentalsForHouse(@PathParam("houseID") String houseID) {
        ServiceResponse<List<RentalDAO>> response = rentalService.getRentalsForHouse(houseID);

        if (response.getItem().isEmpty())
            throw new NotFoundException("No rentals for the given house found.");

        List<Rental> rentals = response.getItem().get()
                .stream()
                .map(Rental::new)
                .toList();

        return Response.ok(rentals).build();
    }

    /**
     * If rental with given id exists, return rental as JSON
     *
     * @param houseID  the id of the house to which the rental belongs
     * @param rentalID the id of the rental to be fetched
     * @return Response with rental JSON for given id in body
     */
    @GET
    @Path("/{rentalID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRentalByID(@PathParam("houseID") String houseID, @PathParam("rentalID") String rentalID) {
        ServiceResponse<RentalDAO> response = rentalService.getByID(rentalID);

        if (response.getItem().isEmpty())
            throw new NotFoundException("Rental with the given id does not exist");

        Rental rental = new Rental(response.getItem().get());

        return Response.ok(rental).build();
    }

    /**
     * Delete a rental by a given id
     *
     * @param rentalID of the rental to be deleted
     * @return nothing - 2xx if delete succeeded
     */
    @DELETE
    @Path("/{rentalID}")
    public Response deleteRental(@CookieParam("scc:session") Cookie session,
                                 @PathParam("houseID") String houseID,
                                 @PathParam("rentalID") String rentalID) {
        ServiceResponse<RentalDAO> rentalResponse = rentalService.getByID(rentalID);

        if (rentalResponse.getItem().isEmpty())
            return Response.status(404).build();

        if (session == null || session.getValue() == null ||
                userService.userSessionInvalid(session.getValue(), rentalResponse.getItem().get().getTenantID()))
            return Response.status(401).build();

        ServiceResponse<Object> response = rentalService.deleteByID(rentalID);

        return Response.status(response.getStatusCode()).build();
    }
}
