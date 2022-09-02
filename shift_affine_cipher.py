# Code to decrypt shift ciphers, or to encrypt/decrypt affine ciphers, given a plain text.

from math import gcd


decipher = input("Enter string: ")
task = input("Enter 1 for shift or 2 for affine: ")
news = ""
shift = 1

if task == "1":
    # shift through the whole alphabet
    while shift <= 26:

        for i in range(len(decipher)):
            curr = (((ord(decipher[i]) - ord('a')) + shift) % 26)
            
            news += chr(curr + ord('a'))
        print("shift {}: {}".format(shift, news))
        news = ""
        shift +=1
else:
    freq = [0] * 26
    for i in range(len(decipher)):
        num = ord(decipher[i]) - ord('a')
        freq[num]+= 1
    
   

    task2 = input("Enter 1 for encrypt or 2 for decrypt: ")

    if task2 == "1":
        a,b = input("Enter key (a,b) ").split()
        for i in range(len(decipher)):
            curChar = ord(decipher[i]) - ord('a')
            curr = ((int(a) * curChar) + int(b)) % 26
            news += chr(curr + ord('a'))
        print("encrypted string: {}".format(news))
    else:
        for i in range(26):
            curchar = chr(i + ord('a'))
            print("{}:letter {} freq is {}".format(i,curchar, freq[i]))
        
        a,b = input("Enter key (a,b) ").split()
        #get mod inverse of a
        inv = pow(int(a),-1,26)
        print("mod inv of a: {}".format(inv))

        for i in range(len(decipher)):
            curChar = ord(decipher[i]) - ord('a')
            curr = (inv * (curChar - int(b))) % 26
            news += chr(curr + ord('a'))
        print("decrypted string: {}".format(news))



    
    
    







