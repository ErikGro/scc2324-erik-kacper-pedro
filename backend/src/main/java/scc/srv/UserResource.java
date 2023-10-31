package scc.srv;

import scc.db.blob.BlobLayer;

import java.net.URI;
import java.util.ArrayList;

import java.util.Optional;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import scc.cache.ServiceResponse;
import scc.cache.UserService;
import scc.data.HouseIds;

import scc.data.UserDAO;
import scc.utils.Hash;

@Path("/user")
public class UserResource {
    private final UserService userService = new UserService();

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserDAO user) {
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setPwd(Hash.of(user.getPwd()));
        ServiceResponse<UserDAO> res = userService.upsert(user);

        if (res.getStatusCode() != 201)
            return Response.status(res.getStatusCode()).build();

        return Response.created(URI.create("/user/" + id)).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
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
    public Response deleteUser(@PathParam("id") String id) {

        Optional<UserDAO> user = userService.getByID(id).getItem();
        if (user.isEmpty())
            return Response.status(400).entity("No such user").build();

        //TODO: add compatibility with houses to show that the user has been deleted
        userService.deleteByID(id);

        return Response.ok(id).build();
    }

    //TODO: transactions
    @Path("/{id}/")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") String id, UserDAO data) {

        data.setId(id);
        ServiceResponse<UserDAO> res = userService.upsert(data);

        return Response.status(res.getStatusCode()).build();
    }

    @Path("/{id}/photo")
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadPhoto(@PathParam("id") String id, byte[] photo) {

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
