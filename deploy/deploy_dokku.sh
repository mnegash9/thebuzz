#!/bin/bash
set -x
git branch -D deploy-dokku
git push -d origin deploy-dokku
read -p "Go to backend branch"
git switch backend
read -p "Create new branch"
cd ..
git switch --create deploy-dokku
read -p "Remove files"
rm -rf README.md admin-cli/ docs/ mobile/ web/
read -p "Press enter to continue"
mv ./backend/* ./
rm -rf backend/
git status
read -p "Press enter to commit and push"
git add .
git commit -m "backend as root of dokku"
git push
git push dokku deploy-dokku:main -f
