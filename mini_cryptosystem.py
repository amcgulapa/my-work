# Compute the encryption of the plaintext A349 with the key 5DF7

plaintext = "A349"
inputkey = "5DF7"

scale = 16 
num_of_bits = 8
# Convert hex to binary
resPlaintext = bin(int(plaintext, 16))[2:].zfill(len(plaintext) * 4)

print(resPlaintext)

# Step 1: Compute C0 = A(P), where A is the permutation matrix shown below. 
t = [[3,7,12,9],
    [11,14,6,1],
    [15,16,10,13],
    [2,4,5,8]]

res = [0] * 16
count = 0
for i in range(4):
    for j in range(4):
        print("{}: bit {} in position {}".format(count+1, resPlaintext[t[i][j]-1], t[i][j]))
        res[count] = resPlaintext[t[i][j]-1]
        count +=1 
print(res)
c0 = ("".join(str(x) for x in res))
print(c0)

# Step 2: Compute C1 = C0 ⨁ K, where K is the input key.
resKey = bin(int(inputkey, 16))[2:].zfill(len(inputkey) * 4)
k = ("".join(str(x) for x in resKey))
print((k))

c1 =int(c0,2) ^ int(k,2)
# print('{0:b}'.format(c1))


# Step 3: Compute C2 = S(C1)
sc1 = str(format(c1,'010b'))
print(sc1)
#  traverse by column
b = [[6,1,11,4],
    [13,12,15,8],
    [0,3,10,9],
    [7,2,5,14]]

# split into chunks of four bits
n=4
block = [sc1[i:i+n] for i in range(0, len(sc1), n)]
print(block)

c2 = []
for bits in block:
    search = (int(bits,2))
    count = 0
    sub = 0

    for i in range(4):
        for j in range(4):
            if count == search:
                print(b[j][i])
                c2.append(b[j][i])
            # print(b[j][i])
            count +=1
    
print(c2)
for i in range(len(c2)):
    print(format(c2[i],'04b'))


#  Step 4: Let c2 = LR where L is left byte and R is right byte.
#  let C = RL
for i in range(2):
    temp = c2[i]
    c2[i] = c2[i+2]
    c2[i+2] = temp
print(c2)
c = []

for i in range(len(c2)):
    c.append(str(format(c2[i],'04b')))
print(c)
        
c = ("".join(str(x) for x in c))
print(c)

c = int(c, 2)
print(c)
c = hex(c)

print(c)