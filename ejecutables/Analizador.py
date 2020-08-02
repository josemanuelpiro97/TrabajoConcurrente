import re
import numpy as np

regex1 = re.compile(r"(T0),(?:.)*?(?:(?:(T1),(?:.)*?(T3),(?:.)*?(?:(?:(T13),(?:.)*?(T7),(?:.)*?)|(?:(T5),(?:.)*?))(?:(?:(T9),(?:.)*?(T15),)|(?:(T10),(?:.)*?(T16))))|(?:(T2),(?:.)*?(T4),(?:.)*?(?:(?:(T14),(?:.)*?(T8),(?:.)*?)|(?:(T6),(?:.)*?))(?:(?:(T11),(?:.)*?(T15),)|(?:(T12),(?:.)*?(T16),))))")

f = open ('../results/shotList.txt','r')
text_log = f.read()
f.close() 

def matcher(match):
    #generate a editable string array
    newString = np.zeros(len(match.group(0)),dtype='object')
    for i in range (0,len(match.group(0))):
        newString[i] = match.group(0)[i]

    # reference index and case_val
    firs_index = match.start(1)

    #replacer
    for i in range (1,len(match.groups())+1):
        index1 = match.span(i)[0]-firs_index
        index2 = match.span(i)[1]-firs_index
        if(index1 >= 0):
            for j in range (index1,index2):
                newString[j] = ''

    #generate a new string to return
    separator = ''
    resultado = separator.join(newString)
    
    return resultado
        

loop=1000
for i in range(0,loop):
    text_log = re.sub(regex1,lambda m: matcher(match=m),text_log,count=1)

    
print(text_log)
print('=====================================================')
print('=====================================================')





