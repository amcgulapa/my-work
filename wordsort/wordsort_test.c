// Andrei Gulapa
// 11/09/2021
// Program to create a binary tree that allows you to analyze the frequency of words in various ways

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LENGTH 21
typedef struct info {
    char word[LENGTH];
    int frequency;
} info;

typedef struct node{
    info *details;
    int depth;
    struct node *left;
    struct node *right;
} node;

void query(node *root, char *input)
{
    if(root == NULL){
        printf("-1 -1\n");
        return;
    }

    int compare = strcmp(input, root->details->word);

    // look for the word in the tree
    if (compare < 0){
        // go left
        query(root->left, input);
    } else if (compare > 0) {
        // go right
        query(root->right, input);
    } else if (compare == 0) {
        // similar word node, so just add 1 to word frequency
        printf("%d %d\n", root->details->frequency, root->depth);
    }
}

node* insert(node* root, char *input, int *arraySize, int *depthCount)
{
    // inserting into an empty tree.
    if (root == NULL) {
        // keeping count of how many new nodes are being made to be used when we make our array
        *arraySize += 1;
        node* temp = malloc(sizeof(node));
        temp->details = malloc(sizeof(info));
        //temp->details->word = malloc((strlen(input) + 1) * sizeof(char));
        strcpy(temp->details->word, input);

        temp->depth = *depthCount;
        temp->details->frequency = 1;

        temp->left = NULL;
        temp->right = NULL;
        return temp;
    }

    // put words in the tree by using strcmp logic
    int compare;
    compare = strcmp(input, root->details->word);

    // keeping count of the depth when going through the tree to add it to our node if needed
    if (compare < 0){
        *depthCount += 1;
        root->left = insert(root->left, input, arraySize, depthCount);
    } else if (compare > 0) {
        *depthCount += 1;
        root->right = insert(root->right, input, arraySize, depthCount);
    } else if (compare == 0) {
        // similar word node, so just add 1 to word frequency
        root->details->frequency += 1;
    }

    // return root of tree
    return root;
}

void copyArray(node *root, info **array, int *counter)
{
    if (root == NULL) return;

    // add elements to our array through recursive calls, adding by 1 to counter for each recursive call through pointer arithmetic
    array[*counter] = root->details;
    *counter += 1;

    copyArray(root->left, array, counter);
    copyArray(root->right, array, counter);
}

info **makeArray(node *root, int arraySize, int *counter)
{
    info **array = NULL;

    // make array of pointers
    array = malloc(arraySize * sizeof(info*));

//    for (int i = 0; i < arraySize; i++)
//    {
//        // size each dynamically
//        array[i] = calloc(1, sizeof(info));
//    }

    copyArray(root, array, counter);

    return array;
}

void merge(info **array, int low, int mid, int high)
{
    // calculate temp length array of pointers and allocate space for it
    int length = high - low + 1;
    info **temp = malloc(length *sizeof(info*));
//    for (int i = 0; i < length; i++)
//    {
//        temp[i] = calloc(1, sizeof(info));
//    }

    // set l_pointer as starting point of left sub-array, set r_pointer as starting point of right sub-array
    int l_pointer = low;
    int r_pointer = mid;
    // set the index for the temp array to 0
    int ind = 0;

    // while pointers are still within their sub-arrays
    while((l_pointer < mid) && (r_pointer <= high))
    {
        // l_pointer has a lesser value, put it in temp array
        if(array[l_pointer]->frequency > array[r_pointer]->frequency)
        {
            temp[ind] = array[l_pointer];
            ind++;
            l_pointer++;

        }
        // case where freq is equal, then we compare alphabetically by word instead
        else if (array[l_pointer]->frequency == array[r_pointer]->frequency)
        {
            int compare = strcmp(array[l_pointer]->word, array[r_pointer]->word);

            if(compare < 0) {
                // word in l_pointer is lower (ASCII)
                temp[ind] = array[l_pointer];
                ind++;
                l_pointer++;
            } else {
                // word in r_pointer is lower (ASCII)
                temp[ind] = array[r_pointer];
                ind++;
                r_pointer++;
            }

        }
        else
        {
            // r_pointer found a lesser value and put it in temp array
            temp[ind] = array[r_pointer];
            ind++;
            r_pointer++;
        }
    }

    // l_pointer still hasn't reached the end of left sub-array so we take its content and put it in the temp array
    while(l_pointer < mid)
    {
        temp[ind] = array[l_pointer];
        ind++;
        l_pointer++;
    }

    // r_pointer still hasn't reached the end of right sub-array so we take its content and put it in the temp array
    while(r_pointer <= high)
    {
        temp[ind] = array[r_pointer];
        ind++;
        r_pointer++;
    }

    // put the sorted contents of temp array in our array
    for(int i = low; i <= high; i++)
    {
        array[i] = temp[i - low];
    }

    // free temp array of pointers
//    for(int i = 0; i < length; i++)
//    {
//        free(temp[i]);
//    }
    free(temp);
}

void mergeSort(info **array, int low, int high)
{
    int mid;
    if(low < high)
    {
        mid = (low + high) / 2;

        mergeSort(array, low, mid);
        mergeSort(array, mid+1, high);

        // once sorted to left and right sub-array we merge them
        merge(array, low, mid+1, high);
    }
}

void freeArray(info **array, int arraySize)
{
    for(int i = 0; i < arraySize; i++)
    {
        free(array[i]);
    }

    free(array);
}

void freeTree(node *root)
{
    if (root != NULL) {
        freeTree(root->left);
        freeTree(root->right);

        //free(root->details->word);
        free(root);
    }
}

int main() {
    int cases = 0, arraySize = 0, counter = 0, action;
    char input[LENGTH];
    node *root = NULL;

    // scan for num of cases
    scanf("%d", &cases);

    for(int i = 0; i < cases; i++)
    {
        int depthCount = 0;

        // scan for user inquiry
        scanf("%d %s", &action, input);

        if(action == 1) {
            // call insert function
            // passing address of arraySize and depthCount so it can be changed within the function
            root = insert(root, input, &arraySize, &depthCount);
        } else if (action == 2) {
            // call query function
            query(root, input);
        }
    }

    // make array of pointers to struct and create an array based on the contents of our binary tree
    info **array = makeArray(root, arraySize, &counter);

    // sort array
    mergeSort(array, 0, arraySize-1);

    // print sorted array
    for (int i = 0; i < arraySize; i++)
    {
        printf("%s %d\n", array[i]->word, array[i]->frequency);
    }

    //free array and tree

    freeTree(root);
    //free(array);
    freeArray(array, arraySize);

    return 0;
}

