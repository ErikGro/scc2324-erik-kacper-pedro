package scc.srv;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import scc.cache.ServiceResponse;
import scc.cache.UserService;
import scc.data.LoginCredentials;
import scc.data.User;
import scc.data.UserDAO;
import scc.db.blob.BlobService;
import scc.utils.Hash;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Path("/user")
public class UserResource {
    private final UserService userService = new UserService();
    private final BlobService blobService = BlobService.getInstance();

    /**
     * Create a new user
     * @param credentials of the new user
     * @return 201 and path if succeeded
     */
    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(LoginCredentials credentials) {
        ServiceResponse<UserDAO> userResponse = userService.getByUsername(credentials.getUsername());
        if (userResponse.getItem().isPresent())
            return Response.status(400).entity("User already exists").build();

        UserDAO user = credentials.toUserDAO();
        String id = UUID.randomUUID().toString();
        user.setId(id);

        ServiceResponse<UserDAO> upsertResponse = userService.upsert(user);

        if (upsertResponse.getStatusCode() != 201)
            return Response.status(upsertResponse.getStatusCode()).build();

        return Response.created(URI.create("/user/" + id)).build();
    }

    /**
     * Update a user for the given id
     * @param session of the user to be updated
     * @param id of the user
     * @param credentials user credentials to be updated
     * @return
     */
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@CookieParam("scc:session") Cookie session, @PathParam("id") String id, LoginCredentials credentials) {
        if (session == null || session.getValue() == null || userService.userSessionInvalid(session.getValue(), id))
            return Response.status(401).build();

        ServiceResponse<UserDAO> userResponse = userService.getByUsername(credentials.getUsername());
        if (userResponse.getItem().isPresent())
            return Response.status(400).entity("User already exists").build();

        UserDAO user = credentials.toUserDAO();
        user.setId(id);

        ServiceResponse<UserDAO> res = userService.upsert(user);

        return Response.status(res.getStatusCode()).build();
    }

    /**
     * Get user of the given id in the path
     * @param id of the user
     * @return
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        ServiceResponse<UserDAO> res = userService.getByID(id);

        if (res.getStatusCode() != 200 || res.getItem().isEmpty())
            return Response.status(res.getStatusCode()).build();

        User user = res.getItem().get().toUser();

        return Response.ok(user).build();
    }

    /**
     * Delete a user for the given id
     * @param session of the user to delete, only the user is eligible to delete itself
     * @param id of the user
     * @return 200 if successful
     */
    @Path("/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@CookieParam("scc:session") Cookie session, @PathParam("id") String id) {
        if (session == null || session.getValue() == null || userService.userSessionInvalid(session.getValue(), id))
            return Response.status(401).build();

        Optional<UserDAO> user = userService.getByID(id).getItem();
        if (user.isEmpty())
            return Response.status(400).entity("No such user").build();

        //TODO: add compatibility with houses to show that the user has been deleted
        userService.deleteByID(id);

        if (user.get().getPhotoID() != null) {
            blobService.getUsersContainer().deleteImage(user.get().getPhotoID());
        }

        return Response.ok().build();
    }

    /**
     * Authenticate a user
     * @param credentials of the user to authenticate
     * @return 200 and session cookie if valid
     */
    @Path("/auth")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response auth(LoginCredentials credentials) {
        ServiceResponse<UserDAO> res = userService.getByUsername(credentials.getUsername());

        if (res.getStatusCode() != 200 || res.getItem().isEmpty())
            return Response.status(res.getStatusCode()).build();

        UserDAO user = res.getItem().get();

        if (!Hash.of(credentials.getPassword()).equals(user.getPasswordHash()))
            return Response.status(401).build();

        String sessionID = UUID.randomUUID().toString();
        NewCookie cookie = new NewCookie.Builder("scc:session")
                .value(sessionID)
                .path("/")
                .comment("sessionid")
                .maxAge(3600)
                .secure(false)
                .httpOnly(true)
                .build();

        userService.putSession(sessionID, user.getId());

        return Response
                .ok()
                .cookie(cookie)
                .location(URI.create("/user/" + user.getId()))
                .build();
    }

    /**
     * Post a photo for a given user
     * @param session of the user
     * @param id of the user
     * @param photo of the user to be created
     * @return HTTP status code
     */
    @Path("/{id}/photo")
    @POST
    @PUT
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postPhoto(@CookieParam("scc:session") Cookie session, @PathParam("id") String id, byte[] photo) {
        if (session == null || session.getValue() == null || userService.userSessionInvalid(session.getValue(), id))
            return Response.status(401).build();

        Optional<UserDAO> userDAO = userService.getByID(id).getItem();
        if (userDAO.isEmpty())
            return Response.status(400).entity("No such user").build();

        UserDAO user = userDAO.get();

        String photoID = UUID.randomUUID().toString();

        if (user.getPhotoID() != null) {
            photoID = user.getPhotoID(); // Overwrite
        }

        blobService.getUsersContainer().upsertImage(photoID, photo);

        user.setPhotoID(photoID);
        ServiceResponse<UserDAO> response = userService.upsert(user);

        return Response.status(response.getStatusCode()).build();
    }
}
