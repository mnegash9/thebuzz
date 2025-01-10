#!/bin/bash
set -x
git branch -D deploy-dokku
read -p "Press enter to continue"
git switch backend
read -p "Press enter to continue"
cd ..
git switch --create deploy-dokku
read -p "Press enter to continue"
rm -rf README.md admin-cli/ docs/ mobile/ web/
read -p "Press enter to continue"
mv ./backend/* ./
rm -rf backend/
git status
read -p "Press enter to continue"
git add .
git commit -m "backend as root of dokku"
git push dokku deploy-dokku:main -f
