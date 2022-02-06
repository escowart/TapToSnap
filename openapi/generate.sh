#!/bin/sh

# Created by Edwin S. Cowart on 05 February, 2022
# Lab49 Take-Home
# Tap To Snap

OpenAPITools=~/bin/openapitools
OpenAPIGeneratorCLI=$OpenAPITools/openapi-generator-cli

if [ ! -d $OpenAPITools ] || [[ $1 == 'update' ]];
then
  which -s brew
  if [[ $? != 0 ]] ; then
    ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"
  else
      brew update
  fi
  brew install maven
  brew install jq

  mkdir -p $OpenAPITools
  curl https://raw.githubusercontent.com/OpenAPITools/openapi-generator/master/bin/utils/openapi-generator-cli.sh > $OpenAPIGeneratorCLI
  chmod u+x $OpenAPIGeneratorCLI
fi

cd $(dirname "$0")/..


# Delete generated code before regenerating
APP=app/src/main/java/com/lab49/taptosnap
find $APP/apis -type f \
    ! -name 'Apis.kt' \
    -delete
find $APP/models -type f \
    ! -name 'ItemsAndStates.kt' \
    ! -name 'ItemAndState.kt' \
    ! -name 'ItemState.kt' \
    -delete
rm -rf $APP/infrastructure
rm -rf docs


OPENAPI_GENERATOR_VERSION=5.4.0 $OpenAPIGeneratorCLI generate \
  -i openapi/specification/specification.yaml \
  -g kotlin \
  -t openapi/templates \
  -p sourceFolder=app/src/main/java \
  -p packageName=com.lab49.taptosnap \
  -p groupId=com.lab49 \
  -p supportAndroidApiLevel25AndBelow=true \
  -p serializableModel=true \
  -p enumPropertyNaming='PascalCase' \
  -p collectionType='array'
