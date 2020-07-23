#!/bin/bash

# shots list 
cat log.txt | sort -k1 | grep true | awk -F "|" '{print $5}' | grep -E -o 'T[0-9]{1,2}' | tr '\n' ',' > shotList.txt

# shots true list
cat log.txt | sort -k1 | grep true > trueShots.txt


