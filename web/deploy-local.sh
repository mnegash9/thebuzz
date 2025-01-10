#!/bin/bash

# local deploy script for the web front-end

# This file is responsible for preprocessing all TypeScript files, making sure
# all dependencies are up-to-date, copying all necessary files into a
# local web deploy directory, and starting a web server with Jasmine included

# This is the resource folder we will use as the web root
TARGETFOLDER=./target

# step 1: make sure we have someplace to put everything.  We will delete the
#         old folder, and then make it from scratch
echo "Deleting and recreating $TARGETFOLDER"
rm -rf $TARGETFOLDER
mkdir $TARGETFOLDER

# step 2: update our npm dependencies
echo "Updating node dependencies"
npm update

# step 3: compile TypeScript files
echo "Compiling app.ts and apptest.ts"
node_modules/typescript/bin/tsc app.ts --lib "es2015","dom" --target es5 --strict --outFile $TARGETFOLDER/app.js
node_modules/typescript/bin/tsc apptest.ts --lib "es2015","dom" --target es5 --strict --outFile $TARGETFOLDER/apptest.js

# step 4: copy static html and css files
echo "Copying static html and css files"
cp Signin/signin.html $TARGETFOLDER/signin.html
cp Signin/index.html $TARGETFOLDER/index.html
cp website.html $TARGETFOLDER/website.html
cp style.css $TARGETFOLDER/style.css



# step 5: copy Jasmine files to the target folder
echo "Copying Jasmine files"
mkdir -p $TARGETFOLDER/jasmine
cp node_modules/jasmine-core/lib/jasmine-core/jasmine.js $TARGETFOLDER/jasmine/
cp node_modules/jasmine-core/lib/jasmine-core/jasmine-html.js $TARGETFOLDER/jasmine/
cp node_modules/jasmine-core/lib/jasmine-core/boot0.js $TARGETFOLDER/jasmine/
cp node_modules/jasmine-core/lib/jasmine-core/boot1.js $TARGETFOLDER/jasmine/
cp node_modules/jasmine-core/lib/jasmine-core/jasmine.css $TARGETFOLDER/jasmine/

# step 6: copy spec_runner.html to the target folder
echo "Copying spec_runner.html"
cp spec_runner.html $TARGETFOLDER/spec_runner.html

# Debug: List contents of target folder
echo "Contents of $TARGETFOLDER:"
ls -l $TARGETFOLDER

# step final: launch the server.  Be sure to disable caching
echo "Starting local webserver at $TARGETFOLDER"
npx http-server $TARGETFOLDER -c-1 -p 8080
