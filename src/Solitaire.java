package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	private CardNode swap(CardNode previous, CardNode target, CardNode follow){
		previous.next=target.next;
		target.next=follow.next;
		follow.next=target;
		return previous;
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD
		final int SEARCH_INTEGER=27;
		CardNode previous=deckRear;
		CardNode target=deckRear.next;
		CardNode follow=target.next;
		do{
			if(target.cardValue==SEARCH_INTEGER){
				
				previous=this.swap(previous, target, follow);
				if(target==deckRear)
					deckRear=follow;
				else if(follow==deckRear)
					deckRear=target;
				return;
			}
									
			previous=previous.next;
			target=target.next;
			follow=follow.next;
							
		}while(previous!=deckRear);
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    // COMPLETE THIS METHOD
		final int SEARCH_INTEGER=28;
		CardNode previous=deckRear;
		CardNode target=deckRear.next;
		CardNode follow=target.next;
		do{
			if(target.cardValue==SEARCH_INTEGER){
				for (int i=0;i<2;i++){
				
				if(target==deckRear)
					deckRear=follow;
				else if(follow==deckRear)
					deckRear=target;
				previous=this.swap(previous, target, follow);
				
				previous=previous.next;
				follow=target.next;
				}
			}
								
			previous=previous.next;
			target=target.next;
			follow=follow.next;
							
		}while(previous!=deckRear);
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// if the first card is a joker
		int newTarget = 0;
		if(deckRear.next.cardValue==27||deckRear.next.cardValue==28){
			 newTarget=27+(28-deckRear.next.cardValue);
			CardNode target2=deckRear.next.next;
			while(target2.cardValue!=newTarget)
				target2=target2.next;
			deckRear=target2;
			return;
		}
		//if the last card is a joker
		if(deckRear.cardValue==27 || deckRear.cardValue==28){
			 newTarget=27+(28-deckRear.cardValue);
			CardNode target2=deckRear.next;
			while(target2.next.cardValue!=newTarget)
				target2=target2.next;
			deckRear=target2;
			return;
		}
		// general case
		
		CardNode previous=deckRear.next;
		CardNode target1=previous.next;
		while(target1.next!=deckRear){
			if(target1.cardValue==27||target1.cardValue==28){
				newTarget=27+(28-target1.cardValue);
				break;
			}
			target1=target1.next;
			previous=previous.next;
				
		}
		
		CardNode target2=target1.next;
		CardNode follow=target2.next;
		while(target2!=deckRear){
			if(target2.cardValue==newTarget){
				target2.next=deckRear.next;
				deckRear.next=target1;
				deckRear=previous;
				deckRear.next=follow;
			}
			target2=target2.next;
			follow=follow.next;
		}
		
		return;		
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		int lastValue=deckRear.cardValue;
		if(lastValue==27||lastValue==28)
			return;
		CardNode nCard=deckRear;
		CardNode secondLast=deckRear;
		while(secondLast.next!=deckRear)
			secondLast=secondLast.next;
		for(int i=0;i<lastValue;i++)
			nCard=nCard.next;
		secondLast.next=deckRear.next;
		deckRear.next=nCard.next;
		nCard.next=deckRear;
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {

		int key=27;
		//this.printList(deckRear);
		while(key==27||key==28){
			this.jokerA();
			//this.printList(deckRear);
			this.jokerB();
			//this.printList(deckRear);
			this.tripleCut();
			//this.printList(deckRear);
			this.countCut();
			//this.printList(deckRear);
			int firstCard=deckRear.next.cardValue;
			if(firstCard==28)
				firstCard--;
			CardNode temp=deckRear.next;
			for(int i=0;i<firstCard-1;i++)
				temp=temp.next;
			key=temp.next.cardValue;
		}
		
		
	    return key;
	}
	
	private String processString(String s){
		String retString="";
		for(int i=0;i<s.length();i++){
			if(Character.isLetter(s.charAt(i)))
				retString+=s.charAt(i);
		}
			
		return retString;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		
		
		String code="";
		message=this.processString(message);
		for(int i=0;i<message.length();i++){
			char letter=message.charAt(i);
			int letterValue=letter-'A'+1;
			int key=this.getKey();
			letterValue+=key;
			if(letterValue>26)
				letterValue=letterValue-26;
			code=code+(char)(letterValue-1+'A');
		}
			
	    return code;
	    
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String phrase="";
		for(int i=0;i<message.length();i++){
			char letter=message.charAt(i);
			int letterValue=letter-'A'+1;
			letterValue=letterValue-this.getKey();
			if(letterValue<1)
				letterValue+=26;
			phrase=phrase+ (char)(letterValue-1+'A');
		}
		return phrase;
	}
}
