package es.uvigo.ei.sing.mahmi.controller.controllers;

import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.wrappers.ProjectToLoadWrapper;
import es.uvigo.ei.sing.mahmi.controller.Configuration;

public class LoaderServiceController extends RemoteServiceAbstractController {

    private LoaderServiceController(final Configuration config) {
        super(config);
    }

    public static LoaderServiceController loaderServiceController(final Configuration config) {
        return new LoaderServiceController(config);
    }

    public Project load(final ProjectToLoadWrapper toLoad) {
        return post(getTargetFor("loader"), toLoad, Project.class);
    }

}
