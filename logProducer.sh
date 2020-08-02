#!/bin/bash

# shots list 
echo '          ====================          '
echo '     ==============================     '
echo '========================================'
echo '       Bash-Script log file parser      '
echo '========================================'
echo '     ==============================     '
echo '          ====================          '
#set -o xtrace
cat log.txt | sort -k1 | grep true | awk -F "|" '{print $5}' | grep -E -o 'T[0-9]{1,2}' | tr '\n' ',' > shotList.txt

# shots true list
cat log.txt | sort -k1 | grep true > trueShots.txt

# last marck
rows=$(cat trueShots.txt | grep -c -E '.')
cat trueShots.txt | awk -F "|" '{print $7}' | sed -n ${rows}p > lastMarck.txt


set +o xtrace
echo ''
echo ''
echo 'FILES GENERATED:'
echo '------------------> "shotList": a one-line text containing the shots fired '
echo '------------------> "trueShots": text log that contains only be true-shots log '
echo ''
echo ''
read -p "DO YOU WANT ANALIZE THE SHOT LIST? (y/n): " -n 1 -r
echo   
if [[ $REPLY =~ ^[Yy]$ ]]
then
echo '-' ;
echo '--' ;
echo '---' ;
echo '----> SHOT LIST ANALYZER' ;
echo '' ;
echo '======================================='
set -o xtrace ;
python3 Analizador.py > shotListAnalized.txt;
set +o xtrace ;
echo '======================================='
echo '-' ;
echo '--' ;
echo '---' ;
echo '----> result in shotListAnalized.txt' ;
fi
read -p "DO YOU WANT TEST THE RESULT? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]
then
echo '-' ;
echo '--' ;
echo '---' ;
echo '----> TEST RESULT' ;
echo '' ;
echo '======================================='
python3 TesterResult.py
fi