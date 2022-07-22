/*=============================================================================
| pa01 - Encrypting a plaintext file using the Vigenere cipher
|
| To Compile:
| gcc -o pa01 pa01.c
|
| To Execute:
| or c -> ./pa01 kX.txt pX.txt
| where kX.txt is the keytext file
| and pX.txt is plaintext file
|
| Note: All input files are simple 8 bit ASCII input
|
+=============================================================================*/

#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SIZE 512

void cipherText(char *key, char *plainText, int keySize);
char *readKey(char *filename, int *keySize);
char *readText(char *filename);
void printKey(char *key, int keySize);
void printText(char *plainText);

int main(int argc, char *argv[])
{
    char *key, *plainText;
    int keySize;

    // pass address of keySize so we can change it' value in the function later
    key = readKey(argv[1], &keySize);

    printf("\n\n");
    printKey(key, keySize);

    plainText = readText(argv[2]);
    printText(plainText);

    cipherText(key, plainText, keySize);

    free(key);
    free(plainText);
}

void cipherText(char *key, char *plainText, int keySize)
{
    int i = 0, j = 0, keyChar, textChar, cipherChar;

    printf("Ciphertext:\n\n");
    while (j < SIZE)
    {
        // convart values to the index value so we have a(0) to z(25)
        keyChar = key[i] - 'a';
        textChar = plainText[j] - 'a';

        // take mod value just in case if the letter goes above 26 (only have 26 letters)
        cipherChar = (keyChar + textChar) % 26;

        // add back 'a' so when we print them out we get the ASCII value
        printf("%c", cipherChar + 'a');

        i++;
        // if we reach end of key, loop back to the beginning
        if (i == keySize)
        {
            i = 0;
        }

        j++;
        // put a newspace after 80 characters
        if (j % 80 == 0)
        {
            printf("\n");
        }
    }
    printf("\n");
}

char *readKey(char *filename, int *keySize)
{
    char *key = NULL;
    int c;
    int i = 0;

    // make space for array
    key = calloc(SIZE, sizeof(char));

    FILE *ifp = fopen(filename, "r");
    if (ifp == NULL)
    {
        printf("Error opening file \n");
        return 0;
    }
    // reads file until EOF and up until size reaches 512 chars
    while ((c = fgetc(ifp)) != EOF && i < SIZE)
    {
        // skips char if it's a non-letter character
        if (isspace(c))
        {
            continue;
        }
        else if (ispunct(c))
        {
            continue;
        }
        else if (isdigit(c))
        {
            continue;
        }

        // converts char to lowercase if it's lowercase
        if (isupper(c))
        {
            key[i] = tolower(c);
            i++;
        }
        else
        {
            key[i] = c;
            i++;
        }
    }
    fclose(ifp);

    // change the value of keySize to the value of i after reading through the file
    *keySize = i;
    return key;
}

char *readText(char *filename)
{
    char *plainText = NULL;
    int c;
    int i = 0;

    // make space for array
    plainText = calloc(SIZE, sizeof(char));

    FILE *ifp = fopen(filename, "r");
    if (ifp == NULL)
    {
        printf("Error opening file \n");
        return 0;
    }

    // reads file until EOF and up until size reaches 512 chars
    while ((c = fgetc(ifp)) != EOF && i < SIZE)
    {
        // skips char if it's a non-letter character
        if (isspace(c))
        {
            continue;
        }
        else if (ispunct(c))
        {
            continue;
        }
        else if (isdigit(c))
        {
            continue;
        }

        // converts char to lowercase if it's lowercase
        if (isupper(c))
        {
            plainText[i] = tolower(c);
            i++;
        }
        else
        {
            plainText[i] = c;
            i++;
        }
    }
    fclose(ifp);

    // add padding if plainText is less than 512 chars
    while (i < SIZE)
    {
        plainText[i] = 'x';
        i++;
    }

    return plainText;
}

void printKey(char *key, int keySize)
{
    printf("Vigenere Key:\n");

    for (int i = 0; i < keySize; i++)
    {
        // add newline after 80 chars
        if (i % 80 == 0)
        {
            printf("\n");
        }

        printf("%c", key[i]);
    }
    printf("\n\n\n");
}

void printText(char *plainText)
{
    printf("Plaintext:\n");

    for (int i = 0; i < SIZE; i++)
    {
        // add newline after 80 chars
        if (i % 80 == 0)
        {
            printf("\n");
        }

        printf("%c", plainText[i]);
    }
    printf("\n\n\n");
}
