# Running the Liferay Faces Archetype Integration Tests

The Liferay Faces Archetypes can be run from the command-line.

To run the integration tests on the latest SNAPSHOT archetypes:

1. Navigate to the `archetype` directory:

		cd archetype

2. Build the project:

		mvn clean install

The integration tests can also be run against specific versions.

To run the tests against a specific version:

1. Navigate to the `archetype/test/integration` directory:

		cd archetype/test/integration

2. Build the tests with the `it.archetype.version` property:

		mvn clean install -Dit.archetype.version=5.0.2

To use different versions for specific archetypes, use one or more of the `it.*.archetype.version` properties:

	mvn clean install -Dit.archetype.version=5.0.4 \
		-Dit.bootsfaces.archetype.version=5.0.2 -Dit.butterfaces.archetype.version=5.0.2

To test only some archetypes, use a comma separated whitelist with the `it.archetype.whitelist` properties:

	mvn clean install -Dit.archetype.version=5.0.4 \
		-Dit.archetype.whitelist=alloy,icefaces,jsf,primefaces,richfaces

## Running the Liferay Faces Archetype Selenium Integration Tests

The Liferay Faces Archetype Selenium integration tests can be run from an IDE (such as Eclipse) or the command line. The
test framework expects that the test portlets have already been deployed to a running portal instance and have already
been added to the appropriate pages.

### Generating and Deploying the Test Portlets

The `generate-and-deploy-test-archetype-portlets.sh` script can be used to quickly and easily generate and deploy all
archetype portlets from release or SNAPSHOT builds of the archetype jars.

Before running the `generate-and-deploy-test-archetype-portlets.sh` script, you must navigate into the `archetype`
directory:

	cd archetype

Then run the script:

	generate-and-deploy-test-archetype-portlets.sh

By default the archetype version from the latest source will be used to generate the portlets. In order to use the
last released version, add the "`release`" argument:

	generate-and-deploy-test-archetype-portlets.sh release

By default the generated portlets are built with maven. In order to use gradle to build the portlets, add the "`gradle`"
argument:

	generate-and-deploy-test-archetype-portlets.sh gradle

Both the above arguments can be used together as well:

	generate-and-deploy-test-archetype-portlets.sh release gradle

### Running the Tests

Before running the tests from the command line, you must navigate into the `test/integration` directory:

	cd test/integration

The tests can be activated by using the `selenium` maven profile. To run all the tests (PhantomJS and Liferay are the
default browser and container properties respectively):

	mvn verify -P selenium

Different browsers can be activated via the `chrome`, `firefox`, `jbrowser`, and `htmlunit` maven profiles. For example,
to run the tests on Firefox:

	mvn verify -P selenium,firefox

**Note:** HTMLUnit and [JBrowser](https://github.com/MachinePublishers/jBrowserDriver) may fail to open web pages with
complex JavaScript due to their experimental/buggy JavaScript support. PhantomJS is recommended for testing complex
pages in a headless environment. Chrome (or the slightly slower Firefox) is recommended for testing complex pages in a
normal desktop environment. See the root `pom.xml` file dependencies section for the required versions of each browser.

The `integration.port` property controls which port the browser will navigate to in order to run the tests. For example,
if the portal is running on port `4000`, then the following command would be needed to test the portlets:

    mvn verify -P selenium -Dintegration.port=4000

All of the above properties and profiles can be combined to run tests in more complex scenarios. Here are some examples:

- Run the tests on Firefox against a Liferay Portal instance:

		mvn verify -P selenium,firefox
