#!/bin/sh

ARCHETYPE_VERSION=$(mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
	-q --non-recursive \
	-Dexec.args='${project.version}')
echo "ARCHETYPE_VERSION=$ARCHETYPE_VERSION"

JSF_VERSION=$(mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
	-q --non-recursive \
	-Dexec.args='${faces.api.version}')
echo "JSF_VERSION=$JSF_VERSION"

LIFERAY_VERSION=$(mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
	-q --non-recursive \
	-Dexec.args='${liferay.version}')
echo "LIFERAY_VERSION=$LIFERAY_VERSION"

PORTLET_VERSION=$(mvn org.codehaus.mojo:exec-maven-plugin:1.2.1:exec -Dexec.executable="echo" \
	-q --non-recursive \
	-Dexec.args='${portlet.api.version}')
echo "PORTLET_VERSION=$PORTLET_VERSION"

CDI=""
if [[ "$@" == *"cdi"* ]]; then
    CDI="cdi"
fi
echo "CDI=$CDI"

PORTLET_TYPES="alloy bootsfaces butterfaces icefaces jsf primefaces richfaces"

THICK_OR_THIN="thick"
if [[ "$@" == *"thin"* ]]; then
	PORTLET_TYPES="alloy bootsfaces butterfaces jsf primefaces richfaces"
    THICK_OR_THIN="thin"
fi
echo "THICK_OR_THIN=$THICK_OR_THIN"

LIFERAY_HOME=$PORTALS_HOME/liferay.com/liferay-dxp-$LIFERAY_VERSION-portlet-$PORTLET_VERSION-jsf-$JSF_VERSION-$THICK_OR_THIN
echo "LIFERAY_HOME=$LIFERAY_HOME"
mkdir -p $LIFERAY_HOME/deploy

# First, it is necessary to build from the top, so that pom.xml and parent/pom.xml get installed
# Note that the externalLiferayFacesRepositories profile is necessary so that the gradle-tooling-api will get resolved
mvn clean install -P externalLiferayFacesRepositories
#mvn archetype:update-local-catalog -P externalLiferayFacesRepositories

mkdir -p target
pushd target

for PORTLET_TYPE in $PORTLET_TYPES; do
	echo $PORTLET_TYPE
	ARTIFACT_ID="com.mycompany.my.$PORTLET_TYPE.portlet"
	rm -rf $ARTIFACT_ID
	mvn -P externalLiferayFacesRepositories --batch-mode archetype:generate \
		-DarchetypeCatalog=local \
		-DgroupId=com.mycompany \
		-Dversion="1.0.0" \
		-DartifactId=$ARTIFACT_ID \
		-DarchetypeGroupId=com.liferay.faces.archetype \
		-DarchetypeArtifactId=com.liferay.faces.archetype.$PORTLET_TYPE.portlet \
		-DarchetypeVersion=$ARCHETYPE_VERSION \
		-DinteractiveMode=false
    rc=$?
    if [ $rc -ne 0 ]; then
        exit $rc
    fi
done

mvn -P externalLiferayFacesRepositories archetype:crawl

for PORTLET_TYPE in $PORTLET_TYPES; do
	echo $PORTLET_TYPE
	ARTIFACT_ID="com.mycompany.my.$PORTLET_TYPE.portlet"
	pushd $ARTIFACT_ID
	mvn clean package -P $THICK_OR_THIN,$CDI,externalLiferayFacesRepositories
	cp target/*.war $LIFERAY_HOME/deploy
	popd
done

popd

echo "WARs have been deployed to $LIFERAY_HOME/deploy"
