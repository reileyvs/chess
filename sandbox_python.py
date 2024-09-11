import numpy as np

arrayV=np.array([1,3,-2,4,5])
arrayU=np.array([1,1,-2,1,1])
arrayW=np.array([1,0,1,0,1])
x=np.dot(arrayV,arrayU)/np.dot(arrayU,arrayU)
print(x)
x=x*arrayU
print(x)
y=np.dot(arrayV,arrayW)/np.dot(arrayW,arrayW)
print(y)
y=y*arrayW
print(y)
z=x+y
print(z)