package com.gsoeller.personalization.maps;

import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;

public class UpdateCommand extends EnvironmentCommand<MapsConfiguration> {
	
    public UpdateCommand(Application<MapsConfiguration> application) {
        super(application, "update", "Runs your fucking face");
    }    

    @Override
    protected void run(Environment environment, Namespace namespace, MapsConfiguration configuration) throws Exception {
    	//application.run(configuration, environment);
    }
}