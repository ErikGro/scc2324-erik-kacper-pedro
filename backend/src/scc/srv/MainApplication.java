package scc.srv;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.Application;
import scc.db.CosmosDBLayer; 

public class MainApplication extends Application
{
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> resources = new HashSet<Class<?>>();

	public MainApplication() {
		singletons.add(CosmosDBLayer.getInstance());
		resources.add(HouseResource.class);
		resources.add(QuestionResource.class);
		resources.add(AnswerResource.class);
		resources.add(MediaResource.class);
		resources.add(UserResource.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
