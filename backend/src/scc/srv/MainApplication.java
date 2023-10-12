package scc.srv;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.Application;
import scc.db.CosmosDBLayer;

public class MainApplication extends Application
{
	private Set<Class<?>> resources = new HashSet<Class<?>>();

	public MainApplication() {
		resources.add(HouseResource.class);
		resources.add(MediaResource.class);
		resources.add(UserResource.class);
		resources.add(CosmosDBLayer.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}
}
