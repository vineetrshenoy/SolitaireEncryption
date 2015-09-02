# SolitaireEncryption

Using Circular Linked Lists to implement the simplified Solitaire encryption algorithm by Bruce Schneier. Created for 
CS112-Data Structures at Rutgers University (Spring 2015). For official assignment, click here: http://www.cs.rutgers.edu/courses/112/classes/spring_2015_venugopal/progs/prog2/prog2.html




A (simplified) deck is represented by a circular linked list, with 28 nodes (26 card plus two jokers, which were represented by the numbers 27 and 28). The API is as follows

     public class Solitaire {
     public Solitaire()                       //Assigns a shuffled deck to deckRear . Calls makeDeck()
     public void makeDeck()                   //Creates a new deck with all the cards shuffled
     public void jokerA()                     //See below
     public void jokerB()                     //See below
     public void tripleCut()                  //See below
     public void countCut()                   //See below
     public int getKey()                      //Calls jokerA(), jokerB(), tripleCut(), countCut() and then extracts value. See assignment details for more info
     public String encrypt(String message)    //Encrypts a message
     public String decrypt(String message)    //Decrypts a message





To get the keystream for encryption, the following methods were implemented:

    STEP 1
    jokerA(): Find Joker "A" (27) and move it ONE card down by swapping it with the card below (after) it.
    This results in the following, after swapping 27 with 7 in the starting deck:

    INITIAL DECK:      13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 27 7 14 5 4 28 11 16 6
                                                                         ^^^^
    DECK AFTER STEP 1: 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7 27 14 5 4 28 11 16 6


    STEP 2
    jokerB():Find Joker "B" (28) and move it TWO cards down by swapping it with the cards below (after) it.
    This results in the following, after moving 28 two cards down in the deck that resulted after step 1:
   
    DECK AFTER STEP 1: 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7 27 14 5 4 28 11 16 6
                                                                                     ^^^^^^^^
    DECK AFTER STEP 2: 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7 27 14 5 4 11 16 28 6
                                                                                     ^^^^^^^^
                                                                                     
    
    STEP 3:
    tripleCut():Swap all the cards before the first (closest to the top/front) joker with the cards after
    the second joker:
    
 
    DECK AFTER STEP 2: 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7|27 14 5 4 11 16 28|6
                       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^                    ^                                                     
    DECK AFTER STEP 3: 6|27 14 5 4 11 16 28|13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7 
                       ^                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    If there no cards before the first joker, then the second joker will become the last card in
    the modified deck. Similarly, if there are no cards after the second joker, then the first
    joker will become the first card in the modified deck.

    STEP 4: 
    countCut(): Look at the value of the last card in the deck. Count down that many cards from the
    first card, and move those cards to just before the last card:
    DECK AFTER STEP 3: 6 27 14 5 4 11 16 28 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7 

    DECK AFTER STEP 4: 28 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 6 27 14 5 4 11 16 7 
                                                                            ^^^^^^^^^^^^^^^^^
    If the last card happens to be Joker B (28), use 27 (instead of 28) as its value for this step.

Using these methods, a keystream was created and a user-inputed message was encoded. Decoding followed a similar process. This assignment was intended to help students use pointers correctly and effecitvely.

The assignmet ws completed on February 23rd, 2015
