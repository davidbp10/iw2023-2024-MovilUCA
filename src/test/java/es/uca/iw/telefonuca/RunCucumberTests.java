package es.uca.iw.telefonuca;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;

@IncludeEngines("cucumber")
@SelectClasspathResource("es/uca/iw/telefonuca/")
public class RunCucumberTests {

}

