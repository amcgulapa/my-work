// Andrei Gulapa
// 11/09/2021
// Program to create a binary tree that allows you to analyze the frequency of words in various ways

/**
helpful links:
https://stackoverflow.com/questions/26874194/binary-tree-for-strings-c
https://www.programiz.com/c-programming/library-function/string.h/strcmp
https://stackoverflow.com/questions/29582431/convert-binary-tree-to-array-in-c

checklist:
    done:
    - basic loop case
    - add insert query logic
    - make insert node function
    - make logic for sorting words in binary search tree
    - add frequency logic
    - add query function
    - add depth logic
    - - update -> make array of pointers to structs
    - - add sorted list word logic (sort words by most frequent to least frequent)

    next:


    - add free memory logic


**/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LENGTH 21
#define COUNT 15
typedef struct info {
    char *word;
    int frequency;
} info;

typedef struct node{
    info *details;
    int depth;
    struct node *left;
    struct node *right;
} node;

// test tree printing in 2D
void print2DUtil(node *root, int space, int arraySize)
{
    // Base case
    if (root == NULL)
        return;

    // Increase distance between levels
    space += COUNT;

    // Process right child first
    print2DUtil(root->right, space, arraySize);

    // Print current node after space
    // count
    printf("\n");
    for (int i = COUNT; i < space; i++)
        printf(" ");
    printf("%s, freq: %d, depth: %d\n", root->details->word, root->details->frequency, root->depth);
    printf("\n");
    // Process left child
    print2DUtil(root->left, space, arraySize);
}

// Wrapper over print2DUtil()
void print2D(node *root, int arraySize)
{
   // Pass initial space count as 0
   print2DUtil(root, 0, arraySize);
}

void query(node *root, char *input)
{
    if(root == NULL){
        printf("-1 -1\n");
        return;
    }

    int compare = strcmp(input, root->details->word);

    if (compare < 0){
        query(root->left, input);
    } else if (compare > 0) {
        // Go right.
        query(root->right, input);
    } else if (compare == 0) {
        // Similar word node, so just add 1 to word frequency
        printf("%d %d\n", root->details->frequency, root->depth);
    }
}

node* insert(node* root, char *input, int *arraySize, int *depthCount)
{
    // Inserting into an empty tree.
    if (root == NULL) {
        *arraySize += 1;
        node* temp = malloc(sizeof(node));

        temp->details = malloc(sizeof(info));

        temp->details->word = malloc((strlen(input) + 1) * sizeof(char));
        strcpy(temp->details->word, input);
        temp->depth = *depthCount;
        temp->details->frequency = 1;

        temp->left = NULL;
        temp->right = NULL;
        return temp;
    }

    // Go left.
    int compare;
    compare = strcmp(input, root->details->word);

    if (compare < 0){
        *depthCount += 1;
        root->left = insert(root->left, input, arraySize, depthCount);
    } else if (compare > 0) {
        // Go right.
        *depthCount += 1;
        root->right = insert(root->right, input, arraySize, depthCount);
    } else if (compare == 0) {
        // Similar word node, so just add 1 to word frequency
        root->details->frequency += 1;
    }

    // Must return the root of this tree.
    return root;
}

void copyArray(node *root, info **array, int *counter)
{

    if (root == NULL) return;

    array[*counter] = root->details;
    *counter += 1;

    copyArray(root->left, array, counter);
    copyArray(root->right, array, counter);
}

info **makeArray(node *root, int arraySize, int *counter)
{
    info **array = NULL;

    array = malloc(arraySize * sizeof(info*));

    for (int i = 0; i < arraySize; i++)
    {
        // size each individual string dynamically
        array[i] = calloc(1, sizeof(info));
    }

    copyArray(root, array, counter);

    return array;
}

void merge(info **array, int low, int mid, int high)
{
    // calculate temp length array and allocate space for it
    int length = high - low + 1;
    info **temp = malloc(length *sizeof(info*));
    for (int i = 0; i < length; i++)
    {
        // size each individual string dynamically
        temp[i] = calloc(1, sizeof(info));
    }

    // set l_pointer as starting point of left sub-array, set r_pointer as starting point of right sub-array
    int l_pointer = low;
    int r_pointer = mid;
    // set the index for the temp array to 0
    int ind = 0;

    // while pointers are still within their subarrays
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
                temp[ind] = array[l_pointer];
                ind++;
                l_pointer++;
            } else {
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

    // free temp array
    for (int i = 0; i < length; i++)
    {
        free(temp[i]);
    }

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

        // once sorted to left and right sub array we merge them
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

        free(root->details->word);
        free(root->details);
        free(root);
    }
}

int main() {
    int cases = 0, arraySize = 0, counter = 0, action;
    char input[LENGTH];
    node *root = NULL;

    printf("Enter number of cases: ");
    scanf("%d", &cases);

    for(int i = 0; i < cases; i++)
    {
        int depthCount = 0;

        printf("1 insert or 2 query: ");
        scanf("%d %s", &action, input);

        if(action == 1) {
            printf("Running insert... \n");
            root = insert(root, input, &arraySize, &depthCount);
            print2D(root,arraySize);

        } else if (action == 2) {
            printf("Running query...\n");
            query(root, input);

        }
    }

    // make array of pointers to struct
    info **array = makeArray(root, arraySize, &counter);


    for (int i = 0; i < arraySize; i++)
    {
        printf("Print elem: %s %d\n", array[i]->word, array[i]->frequency);
    }

    printf("\n");
    // sort array
    mergeSort(array, 0, arraySize-1);

    // print array
    for (int i = 0; i < arraySize; i++)
    {
        printf("Print sorted elem: %s %d\n", array[i]->word, array[i]->frequency);
    }

    //free array and tree
    freeArray(array, arraySize);
    freeTree(root);

    return 0;
}

