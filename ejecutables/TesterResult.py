import numpy as np
import json
print('============================================================================================================')
print('============================================================================================================')
print('                                                                                                           =')
print("                                               *************                                               =")
print("                                                     *                                                     =")
print("                                                     *                                                     =")
print("                                                     V                                                     =")
print("                                   ====================================                                    =")
print("                                   WELCOME TO THE PARTIAL RESULT TESTER                                    =")
print("                                   ====================================                                    =")
print("                                                     ⋀                                                     =")
print("                                                     *                                                     =")
print("                                                     *                                                     =")
print("                                               *************                                               =")
print('                                                                                                           =')

def plusVal(index, vector):
    vector[int(index)] = vector[int(index)] + 1
    return vector 

# get the needed files
f = open ('../results/lastMarck.txt','r')
lastMarck = f.read()
f.close() 

f = open ('../results/shotListAnalized.txt','r')
shotListAnalized = f.read()
f.close() 

#build invariant matrix
with open('../TpFinal.json') as json_file:
    data = json.load(json_file)
    matrix = data['matrixI']
    m_0 = data['mark']
  

#build shotVector
shotVector = np.zeros(17,dtype='object')

#complete
for i in range (0,len(shotListAnalized)):
    if(shotListAnalized[i] == 'T'):
        if(shotListAnalized[i+2] != ','):
            index = shotListAnalized[i+1] + shotListAnalized[i+2]
            shotVector = plusVal(index,shotVector)
            i+=2
        else:
            shotVector = plusVal(shotListAnalized[i+1],shotVector)
            i+=1          


print()
print()
print("INCIDENCE MATRIX --------------------------> ",matrix[0])
for i in range (1,len(matrix)):
    print("                                             ",matrix[i])
print()
print("VECTOR SHOT AFTER REMOVE ALL INVARIANT-T --> ",shotVector)
print()
print("INITIAL MARK ------------------------------> ",m_0)
print()

#matrix multiplication
result = np.zeros(len(m_0),dtype='object')
for i in range(0,len(matrix)):
    for j in range (0,len(matrix[i])):
        result[i] += matrix[i][j] * shotVector[j]  
    result[i] += m_0[i]
print()
print()
print('------------------------------------------------------------------------------------------------------------')
print('------------------------------------------------------------------------------------------------------------')
print('[After multiplying the matrix by the trigger vector,]')
print('[and having added the result to the initial mark,]')
print('[we are left with:]')
print()
print("RESULT ------------------------------------> ",result)
print()
print()

print("FINAL MARK--------------------------------->",lastMarck)
print()
print('============================================================================================================')
print('============================================================================================================')

