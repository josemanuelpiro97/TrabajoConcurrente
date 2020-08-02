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
cat ../results/log.txt | sort -k1 | grep true | awk -F "|" '{print $5}' | grep -E -o 'T[0-9]{1,2}' | tr '\n' ',' > ../results/shotList.txt

# shots true list
cat ../results/log.txt | sort -k1 | grep true > ../results/trueShots.txt

# last marck
rows=$(cat ../results/trueShots.txt | grep -c -E '.')
cat ../results/trueShots.txt | awk -F "|" '{print $7}' | sed -n ${rows}p > ../results/lastMarck.txt


set +o xtrace
echo ''
echo ''
echo 'FILES GENERATED:'
echo '------------------> "shotList": a one-line text containing the shots fired '
echo '------------------> "trueShots": text log that contains only be true-shots log '
echo ''
echo ''

#shot analizer
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
python3 Analizador.py > ../results/shotListAnalized.txt;
set +o xtrace ;
echo '======================================='
echo '-' ;
echo '--' ;
echo '---' ;
echo '----> result in ../results/shotListAnalized.txt' ;
fi

# correct behavior tester
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

# statistics
read -p "DO YOU WANT SEE THE STATISTICS? (y/n): " -n 1 -r
echo   
if [[ $REPLY =~ ^[Yy]$ ]]
then
echo '-' ;
echo '--' ;
echo '---' ;
echo '----> STATISTICS' ;
echo ''
echo 'processor balance:'
echo "                  --> Processor-1 times: $(cat ../results/log.txt | sort -k1 | grep true | awk -F "|" '{print $5}' | grep -E -o 'T[0-9]{1,2} ' | grep -c -E '(T1 )') "
echo "                  --> Processor-2 times: $(cat ../results/log.txt | sort -k1 | grep true | awk -F "|" '{print $5}' | grep -E -o 'T[0-9]{1,2} ' | grep -c -E '(T2 )') "

echo 'memory balance:'
#memory1
t9=$(cat ../results/log.txt | sort -k1 | grep true | awk -F "|" '{print $5}' | grep -E -o 'T[0-9]{1,2} ' | grep -c -E '(T9 )')
t11=$(cat ../results/log.txt | sort -k1 | grep true | awk -F "|" '{print $5}' | grep -E -o 'T[0-9]{1,2} ' | grep -c -E '(T11 )')
echo "                --> memory-1 times: $((t9 + t11))"
#memory2 
t10=$(cat ../results/log.txt | sort -k1 | grep true | awk -F "|" '{print $5}' | grep -E -o 'T[0-9]{1,2} ' | grep -c -E '(T10 )')
t12=$(cat ../results/log.txt | sort -k1 | grep true | awk -F "|" '{print $5}' | grep -E -o 'T[0-9]{1,2} ' | grep -c -E '(T12 )')
echo "                --> memory-2 times: $((t10 + t12))"
fi