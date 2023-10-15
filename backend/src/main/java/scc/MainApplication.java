package scc;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.Application;
import scc.srv.HouseResource;
import scc.srv.MediaResource;
import scc.srv.RentalResource;
import scc.srv.UserResource;

public class MainApplication extends Application
{
	private final Set<Class<?>> resources = new HashSet<Class<?>>();

	public MainApplication() {
		resources.add(HouseResource.class);
		resources.add(MediaResource.class);
		resources.add(UserResource.class);
		resources.add(RentalResource.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}
}
