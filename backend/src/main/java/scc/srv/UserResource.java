package scc.srv;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import scc.cache.ServiceResponse;
import scc.cache.UserService;
import scc.data.LoginCredentials;
import scc.data.UserDAO;
import scc.db.blob.BlobLayer;
import scc.utils.Hash;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Path("/user")
public class UserResource {
    private final UserService userService = new UserService();

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserDAO user) {
        ServiceResponse<UserDAO> userResponse = userService.getByUsername(user.getName());
        if (userResponse.getItem().isPresent())
            return Response.status(400).entity("User already exists").build();

        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setPwd(Hash.of(user.getPwd()));

        ServiceResponse<UserDAO> upsertResponse = userService.upsert(user);

        if (upsertResponse.getStatusCode() != 201)
            return Response.status(upsertResponse.getStatusCode()).build();

        return Response.created(URI.create("/user/" + id)).build();
    }

    @Path("/auth")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response auth(LoginCredentials credentials) {
        ServiceResponse<UserDAO> res = userService.getByUsername(credentials.getUsername());

        if (res.getStatusCode() != 200 || res.getItem().isEmpty())
            return Response.status(res.getStatusCode()).build();

        UserDAO user = res.getItem().get();

        if (!Hash.of(credentials.getPassword()).equals(user.getPwd()))
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

        return Response.ok().cookie(cookie).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@CookieParam("scc:session") Cookie session, @PathParam("id") String id) {
        if(session == null || session.getValue() == null || userService.userSessionInvalid(session.getValue(), id))
            return Response.status(401).build();

        ServiceResponse<UserDAO> res = userService.getByID(id);

        if (res.getStatusCode() != 200 || res.getItem().isEmpty())
            return Response.status(res.getStatusCode()).build();

        UserDAO user = res.getItem().get();
        user.setPwd(null);

        return Response.ok(user).build();
    }

    @Path("/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@CookieParam("scc:session") Cookie session, @PathParam("id") String id) {
        if(session == null || session.getValue() == null || userService.userSessionInvalid(session.getValue(), id))
            return Response.status(401).build();

        Optional<UserDAO> user = userService.getByID(id).getItem();
        if (user.isEmpty())
            return Response.status(400).entity("No such user").build();

        //TODO: add compatibility with houses to show that the user has been deleted
        userService.deleteByID(id);

        return Response.ok().build();
    }

    //TODO: transactions
    @Path("/{id}/")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@CookieParam("scc:session") Cookie session, @PathParam("id") String id, UserDAO data) {
        if(session == null || session.getValue() == null || userService.userSessionInvalid(session.getValue(), id))
            return Response.status(401).build();

        data.setId(id);
        ServiceResponse<UserDAO> res = userService.upsert(data);

        return Response.status(res.getStatusCode()).build();
    }

    @Path("/{id}/photo")
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadPhoto(@CookieParam("scc:session") Cookie session, @PathParam("id") String id, byte[] photo) {
        if(session == null || session.getValue() == null || userService.userSessionInvalid(session.getValue(), id))
            return Response.status(401).build();

        Optional<UserDAO> user = userService.getByID(id).getItem();
        if (user.isEmpty())
            return Response.status(400).entity("No such user").build();

        BlobLayer blobLayer = BlobLayer.getInstance();
        blobLayer.usersContainer.uploadImage(id, photo);

        return Response.ok(id).build();
    }

    @Path("/{id}/photo")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getPhoto(@PathParam("id") String id) {

        Optional<UserDAO> user = userService.getByID(id).getItem();
        if (user.isEmpty())
            return Response.status(400).entity("No such user").build();

        BlobLayer blobLayer = BlobLayer.getInstance();

        byte[] photo;
        try {
            photo = blobLayer.usersContainer.getImage(id);
        } catch (Exception e) {
            return Response.status(404).entity("No photo found").build();
        }

        return Response.ok(photo).build();
    }
}
