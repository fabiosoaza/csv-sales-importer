#!/bin/bash

PROPERTIES_FILE="gradle.properties"
REPOSITORY="sales-importer"

function get_artifact_version(){
    ARTIFACT_VERSION=$(grep "${1}" $PROPERTIES_FILE | grep version | cut -d'=' -f2)
    echo $ARTIFACT_VERSION
}

function get_artifact_version_without_classifier(){
    ARTIFACT_VERSION="$(get_artifact_version)"
    VERSION_WITHOUT_QUALIFIER=${ARTIFACT_VERSION//[!0-9.]/}
    echo $VERSION_WITHOUT_QUALIFIER
}

function set_release_version(){
   VERSION="v$(get_artifact_version_without_classifier)"   
   BRANCH_NAME="$(git rev-parse --abbrev-ref HEAD)"
   echo "Generating release version : $VERSION"
   echo "Current Branch: $BRANCH_NAME"
   sed -i "s/\(version=\).*\$/\1${VERSION}/" $PROPERTIES_FILE
}

function set_snapshot_version(){
    VERSION_WITHOUT_QUALIFIER="$(get_artifact_version_without_classifier)" 
    BRANCH_NAME="$(git rev-parse --abbrev-ref HEAD)"
    
    a=( ${VERSION_WITHOUT_QUALIFIER//./ } )                  
    ((a[1]++))            
    SNAPSHOT_VERSION="${a[0]}.${a[1]}.0-SNAPSHOT"
    
    echo "Setting develop version to: $SNAPSHOT_VERSION"
    echo "Current Branch:  $BRANCH_NAME" 
    sed -i "s/\(version=\).*\$/\1${SNAPSHOT_VERSION}/" $PROPERTIES_FILE 
}

function tag_release(){
    RELEASE_VERSION="v$(get_artifact_version_without_classifier)"
    BRANCH_NAME="$(git rev-parse --abbrev-ref HEAD)"
    echo "Current Branch: $BRANCH_NAME"
    git add $PROPERTIES_FILE
    git commit -m '[skip ci] - Generating release version '$RELEASE_VERSION
    git tag -a "$RELEASE_VERSION" -m "Tagging version $RELEASE_VERSION"
    git checkout master
    git merge --ff-only "$BRANCH_NAME" 
}

function tag_snapshot(){    
    SNAPSHOT_VERSION="$(get_artifact_version_without_classifier)-SNAPSHOT"
    BRANCH_NAME="$(git rev-parse --abbrev-ref HEAD)"
    echo "Current Branch: $BRANCH_NAME"

    git add $PROPERTIES_FILE 
    git commit -m '[skip ci] - Setting develop version to '$SNAPSHOT_VERSION 

    git checkout master
    git merge --ff-only "$BRANCH_NAME" 

}


function release(){
   echo "[RELEASE] - Creating temporary branch $BRANCH_NAME" 
   git checkout -b $INTEGRATION_BRANCH 
   set_release_version
   tag_release
   echo "[RELEASE] - Removing temporary branch $BRANCH_NAME"
   git branch -D $INTEGRATION_BRANCH 
}

function start(){
   echo "[SNAPSHOT] - Creating temporary branch $BRANCH_NAME" 
   git checkout -b $INTEGRATION_BRANCH 
   set_snapshot_version
   tag_snapshot
   git branch -D $INTEGRATION_BRANCH
   echo "[SNAPSHOT] - Removing temporary branch $BRANCH_NAME"

}

function push(){
   last_tag=$(git describe --abbrev=0 --tags)  
   git push "https://${GITHUB_TOKEN}@github.com/fabiosoaza/$REPOSITORY" master
   git push "https://${GITHUB_TOKEN}@github.com/fabiosoaza/$REPOSITORY" $last_tag
   git pull

}

function install(){
    echo "Running install task"
    if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
        git fetch --prune
        set_release_version
    fi
    ./gradlew clean build
}

function build_and_push_image(){
    build_image
    push_image
}

function build_image(){
    PROJECT_NAME=$(grep "${1}" $PROPERTIES_FILE | grep name | cut -d'=' -f2)
    IMAGE_NAME="fabiosoaza/${PROJECT_NAME}:$(get_artifact_version)"
    TAG_NAME="${IMAGE_NAME}"
    echo "Building image $IMAGE_NAME"
    docker build . -f Dockerfile -t $IMAGE_NAME
    echo "Tagging image $IMAGE_NAME to $TAG_NAME"
    docker tag $IMAGE_NAME $TAG_NAME
}

function push_image(){
    PROJECT_NAME=$(grep "${1}" $PROPERTIES_FILE | grep name | cut -d'=' -f2)
    IMAGE_NAME="fabiosoaza/${PROJECT_NAME}:$(get_artifact_version)"
    TAG_NAME="${IMAGE_NAME}"
    echo "Pushing image $IMAGE_NAME to DockerHub"    
    echo "$DOCKERHUB_TOKEN" | docker login --username "$DOCKERHUB_USERNAME" --password-stdin
    docker push $TAG_NAME

}

function after_success(){
    echo "Running after sucess task"   
    if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
        echo "Releasing version"
        release
        echo "Sending artifacts to repository"
        build_and_push_image
        echo "Changing properties to next snapshot version"
        start 
        echo "Merging to branch master e sending to scm"
        push  
    else 
        echo "Sending artifacts to repository"
        build_and_push_image
    fi

}


 INTEGRATION_BRANCH="build_$TRAVIS_JOB_NUMBER"

 git config --global user.email 'travis@travis-ci.org'
 git config --global user.name 'Travis'
 git remote set-branches --add origin master
 git fetch


 INTEGRATION_BRANCH="build_$TRAVIS_JOB_NUMBER"

  case $1 in
        "build_image")
             build_image
             ;;
        "get_artifact_version")
             get_artifact_version
             ;;
         "set_release_version")
             set_release_version
             ;;
         "set_snapshot_version")
             set_snapshot_version
             ;;
         "start")
             start
             ;; 
         "install")
             install
             ;;              
         "release")
             release
             ;;       
         "after_success")
             after_success
             ;;                                    
         "push")
             push
             ;;   
   esac