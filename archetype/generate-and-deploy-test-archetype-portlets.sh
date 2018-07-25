#!/bin/bash

buildArchetypeAndGenerateAndDeployTestArchetypePortlet() {

	ARCHETYPE=$1
	LIFERAY_VERSION=$2
	JSF_VERSION=$3
	BUILD_TOOL=$4
	USE_RELEASE_ARCHETYPE_VERSIONS=$5

	cd $ARCHETYPE

	if [ ! -f pom.xml ]; then
		echo "Skipping $ARCHETYPE because no pom.xml file was found."
		return;
	fi

	ARCHETYPE_VERSION=$(mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
		-q --non-recursive \
		-Dexec.args='${project.version}')

	if [ "$USE_RELEASE_ARCHETYPE_VERSIONS" = true ]; then
		ARCHETYPE_MINOR_VERSION=$(echo "$ARCHETYPE_VERSION" | sed -e 's/^\(\([0-9][0-9]*\.\)*\)\([0-9]*\).*$/\3/')
		ARCHETYPE_VERSION=${ARCHETYPE_VERSION//$ARCHETYPE_MINOR_VERSION-SNAPSHOT/$((ARCHETYPE_MINOR_VERSION-1))}
	else
		echo "Building $ARCHETYPE archetype:"
		mvn clean install
	fi

	cd ../target/
	ARCHETYPE_LIBRARY=${ARCHETYPE//[^[:alpha:]]}
	ARCHETYPE_LIBRARY=${ARCHETYPE_LIBRARY//portlet}

	echo "Generating test $ARCHETYPE with archetype version $ARCHETYPE_VERSION:"
	mvn --batch-mode archetype:generate \
		-DgroupId=com.mycompany \
		-DartifactId=com.mycompany.my.$ARCHETYPE_LIBRARY.portlet \
		-DarchetypeGroupId=com.liferay.faces.archetype \
		-DarchetypeArtifactId=com.liferay.faces.archetype.$ARCHETYPE_LIBRARY.portlet \
		-DarchetypeVersion=$ARCHETYPE_VERSION \
		-DinteractiveMode=false
	cd com.mycompany.my.$ARCHETYPE_LIBRARY.portlet

	if [[ "$BUILD_TOOL" == "maven" ]]; then
		echo "Building test $ARCHETYPE:"
		mvn clean package || { echo "Failed to build portlet from $ARCHETYPE."; exit 1; }

		echo "Deploying test $ARCHETYPE:"
		cp target/*.war \
			$HOME/Portals/liferay.com/liferay-portal-$LIFERAY_VERSION-jsf-$JSF_VERSION/deploy/com.mycompany.my.$ARCHETYPE_LIBRARY.portlet.war
	elif [[ "$BUILD_TOOL" == "gradle" ]]; then
		echo "Building test $ARCHETYPE:"
		gradle clean build || { echo "Failed to build portlet from $ARCHETYPE."; exit 1; }

		echo "Deploying test $ARCHETYPE:"
		cp build/libs/*.war \
			$HOME/Portals/liferay.com/liferay-portal-$LIFERAY_VERSION-jsf-$JSF_VERSION/deploy/com.mycompany.my.$ARCHETYPE_LIBRARY.portlet.war
	fi
}

mvn clean
mkdir -p ./target/

BUILD_TOOL="maven"

if [[ "$@" == *"gradle"* ]]; then
	BUILD_TOOL="gradle"
fi

USE_RELEASE_ARCHETYPE_VERSIONS=false

if [[ "$@" == *"release"* ]]; then
	USE_RELEASE_ARCHETYPE_VERSIONS=true
fi

LIFERAY_VERSION=$(mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
	-q --non-recursive \
	-Dexec.args='${liferay.version}')

JSF_VERSION=$(mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
	-q --non-recursive \
	-Dexec.args='${faces.api.version}')

# If possible build each archetype and generate and deploy a portlet based on the newly built archetype in parallel.
if hash parallel 2>/dev/null; then
	export -f buildArchetypeAndGenerateAndDeployTestArchetypePortlet
	parallel --no-notice --max-args=1 \
		buildArchetypeAndGenerateAndDeployTestArchetypePortlet {} \
			$LIFERAY_VERSION $JSF_VERSION $BUILD_TOOL $USE_RELEASE_ARCHETYPE_VERSIONS ::: ./*-portlet
else
	for ARCHETYPE in ./*-portlet; do
		(buildArchetypeAndGenerateAndDeployTestArchetypePortlet \
			$ARCHETYPE $LIFERAY_VERSION $JSF_VERSION $BUILD_TOOL $USE_RELEASE_ARCHETYPE_VERSIONS)
	done
fi
