package scc;

import java.util.HashSet;
import java.util.Set;
import jakarta.ws.rs.core.Application;
import scc.srv.*;

public class MainApplication extends Application {
	private final Set<Class<?>> resources = new HashSet<>();

	public MainApplication() {
		resources.add(HouseResource.class);
		resources.add(RentalResource.class);
		resources.add(QuestionResource.class);
		resources.add(AnswerResource.class);
		resources.add(UserResource.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}
}
