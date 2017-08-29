#!/bin/bash

buildArchetypeAndGenerateAndDeployTestArchetypePortlet() {

	ARCHETYPE=$1
	LIFERAY_VERSION=$2
	JSF_VERSION=$3

	echo "Building $ARCHETYPE archetype:"
	cd $ARCHETYPE
	ARCHETYPE_VERSION=$(mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
		-q --non-recursive \
		-Dexec.args='${project.version}')
	mvn clean install
	cd ../target/
	ARCHETYPE_LIBRARY=${ARCHETYPE//[^[:alpha:]]}
	ARCHETYPE_LIBRARY=${ARCHETYPE_LIBRARY//portlet}

	echo "Generating test $ARCHETYPE:"
	mvn archetype:generate \
		-DgroupId=com.mycompany \
		-DartifactId=com.mycompany.my.$ARCHETYPE_LIBRARY.portlet \
		-DarchetypeGroupId=com.liferay.faces.archetype \
		-DarchetypeArtifactId=com.liferay.faces.archetype.$ARCHETYPE_LIBRARY.portlet \
		-DarchetypeVersion=$ARCHETYPE_VERSION \
		-DinteractiveMode=false
	cd com.mycompany.my.$ARCHETYPE_LIBRARY.portlet

	echo "Building test $ARCHETYPE:"
	mvn clean package

	echo "Deploying test $ARCHETYPE:"
	cp target/*.war \
		$HOME/Portals/liferay.com/liferay-portal-$LIFERAY_VERSION-jsf-$JSF_VERSION/deploy/com.mycompany.my.$ARCHETYPE_LIBRARY.portlet.war
}

mvn clean
mkdir -p ./target/

LIFERAY_VERSION=$(cd ../bridge-ext && mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
	-q --non-recursive \
	-Dexec.args='${liferay.version}')

JSF_VERSION=$(cd ../bridge-ext && mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
	-q --non-recursive \
	-Dexec.args='${faces.api.version}')

# If possible build each archetype and generate and deploy a portlet based on the newly built archetype in parallel.
if hash parallel 2>/dev/null; then
	export -f buildArchetypeAndGenerateAndDeployTestArchetypePortlet
	parallel --no-notice --max-args=1 \
		buildArchetypeAndGenerateAndDeployTestArchetypePortlet {} $LIFERAY_VERSION $JSF_VERSION ::: ./*-portlet
else
	for ARCHETYPE in ./*-portlet; do
		(buildArchetypeAndGenerateAndDeployTestArchetypePortlet $ARCHETYPE $LIFERAY_VERSION $JSF_VERSION)
	done
fi
