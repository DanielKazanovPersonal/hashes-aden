import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicIntHashLP1Test.
 */
@TestMethodOrder(OrderAnnotation.class)
class BasicIntHashLP1Test {
	private final boolean DEBUG = false;
	private MyIntHash hash;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Basic init test. Check that every entry is initialized to -1
	 */
	@Test
	@Order(1)
	void BasicInit_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #1: Hash Table Initialization");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) 
			assertEquals(-1,hash.getHashAt(i, 0));
	}

	/**
	 * Basic hash func test. Tests the basic functionality of the hash function
	 * by adding keys from 0 to size -1 of the hash. Tests that entries are where 
	 * they should be. No collisions.
	 */
	@Test
	@Order(2)
	void BasicHashFunc_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #2: Hash Function ");
		int size = hash.getTableSize();
		assertEquals(31,size);
		assertEquals(0,hash.size());
		assertTrue(hash.isEmpty());
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(i));
			assertEquals(i, hash.getHashAt(i, 0));
			assertFalse(hash.isEmpty());
		}
		assertEquals(size,hash.getTableSize());
		
		hash.clear();
		assertEquals(0, hash.size());
		assertTrue(hash.isEmpty());
		for (int i = 0; i < size; i++) {
			assertEquals(-1,hash.getHashAt(i, 0));
		}
	}
	
	
	/**
	 * Basic hash func rand test. Generates random values for each index
	 * of the hash table with no collisions. Validates that each value is
	 * placed in the appropriate location.
	 */
	@Test
	@Order(3)
	void BasicHashFuncRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		Random random = new Random();
		System.out.println("Basic Test #3: Hash Function - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] rndBasePlusIndex = new int[size];
		System.out.println("   Generating key: (random base * table size) + index");
		for (int i = 0; i < size; i++ ) {
			rndBasePlusIndex[i] = random.nextInt(1024)*size+i;
			System.out.println("   Index = "+i+"  key = "+rndBasePlusIndex[i]+"   hash index = "+(rndBasePlusIndex[i]%size));
		}
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(rndBasePlusIndex[i]));
			assertEquals(rndBasePlusIndex[i],hash.getHashAt(i, 0));
		}
		assertEquals(size,hash.getTableSize());
	}
	
	/**
	 * Basic hash contains test. Checks that the contains function works properly, demonstrating
	 * that:
	 * 		entry does not exist before being added
	 * 		entry exists after being added
	 *  	entry is in the correct location of the hash
	 *  
	 *  No collisions in this test. Data added is 0 to (size -1)
	 */
	@Test
	@Order(4)
	void BasicHashContains_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #4: Hash contains method");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) {
			System.out.println("   Before Add: Checking that hash does not contain "+i);
			assertFalse(hash.contains(i));
			assertTrue(hash.add(i));
			System.out.println("   After Add:  Checking that hash does contain "+i);
			assertTrue(hash.contains(i));
			System.out.println("   After Add:  Checking that hash does contain "+i+" at index "+i);
			assertEquals(i,hash.getHashAt(i, 0));
		}
	}
	
	
	/**
	 * Basic hash contains rand test. Same concept as test #4, but with random data that is
	 * guaranteed to avoid collisions
	 */
	@Test
	@Order(5)
	void BasicHashContainsRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		Random random = new Random();
		System.out.println("Basic Test #3: Hash Function - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] rndBasePlusIndex = new int[size];
		System.out.println("   Generating key: (random base * table size) + index");
		for (int i = 0; i < size; i++ ) {
			rndBasePlusIndex[i] = random.nextInt(1024)*size+i;
			System.out.println("   Index = "+i+"  key = "+rndBasePlusIndex[i]+"   hash index = "+(rndBasePlusIndex[i]%size));
		}
		for (int i = 0; i < size; i++) {
			System.out.println("   Before Add: Checking that hash does not contain "+rndBasePlusIndex[i]);
			assertFalse(hash.contains(rndBasePlusIndex[i]));
			assertTrue(hash.add(rndBasePlusIndex[i]));
			System.out.println("   After Add:  Checking that hash does contain "+rndBasePlusIndex[i]);
			assertTrue(hash.contains(rndBasePlusIndex[i]));
			System.out.println("   After Add:  Checking that hash does contain "+rndBasePlusIndex[i]+" at index "+i);
			assertEquals(rndBasePlusIndex[i],hash.getHashAt(i, 0));
		}
	}
	
	/**
	 * Basic hash collision test. Picking index 3 as default, fills the hash sequentially by
	 * adding the key (initial value 3), and then adding size to generate subsequent keys. All
	 * will collide, and should find a home using linear probing. Entries are checked for proper
	 * location and values
	 */
	@Test
	@Order(6)
	void BasicHashCollision_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #6: Hash Collision handling method");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int index = 3;
		for (int i = 0; i < size; i++) {
			int key = i*size + index;
			System.out.println("   Before Add: Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
			System.out.println("   After Add:  Checking that hash does contain "+key);
			assertTrue(hash.contains(key));
			System.out.println("   After Add:  Checking that hash does contain "+key+" at index "+((index+i)%size));
			assertEquals(key,hash.getHashAt(((index+i)%size), 0));
		}
	}
	
	/**
	 * Basic hash collision rand test.  Same as test #6 - the hash is filled with randomized
	 * values that are all unique and collide. Checks to see that entries are in proper locations and
	 * order.
	 */
	@Test
	@Order(7)
	void BasicHashCollisionRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		Random random = new Random();
		System.out.println("Basic Test #7: Hash Collision handling - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		ArrayList<Integer> randInts = new ArrayList<Integer>();
		int rndIndex = random.nextInt(size); // generate the random index;
		System.out.println("   Generating key: (random base * table size) + index");
		while (randInts.size() < size) {
			Integer randomInt = random.nextInt(1024);
			if (!randInts.contains(randomInt)) {
				System.out.println("   i = "+randInts.size()+"  index = "+randomInt);
				randInts.add(randomInt);
			}
		}
		
		for (int i = 0; i < size; i++) {
			int key = randInts.get(i)*size + rndIndex;
			System.out.println("   Before Add: Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
			System.out.println("   After Add:  Checking that hash does contain "+key);
			assertTrue(hash.contains(key));
			System.out.println("   After Add:  Checking that hash does contain "+key+" at index "+((i+rndIndex)%size));
			assertEquals(key,hash.getHashAt(((i+rndIndex)%size), 0));
		}
	}

	
	/**
	 * Basic add - full Test.  This test operates on the following principle:
	 * 
	 * 1) completely fill the hash table (no collisions). Ensure that data goes to the correct
	 *    location and that size grows as expected.
	 * 2) attempt to add a new element to the hash at each table location. Check that
	 *    add fails and that size (# of elements in the hash) is unchanged.
	 *    
	 *    NOTE that this is a corner case test; the load factor will never let this happen
	 *    under normal operation - the hash will grow first.
	 */
	@Test
	@Order(8)
	void BasicAddFull_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #14: Add Failures due to full table");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) { 
			int numElements = hash.size();
			assertTrue(hash.add(i));
			assertTrue((numElements+1)==hash.size());
			assertEquals(i,hash.getHashAt(i, 0));
		}
		if (DEBUG) printHash();
		
		int numElements = hash.size();
		for (int i = 0; i < size; i++) {
			System.out.println("   Attempting to add "+(i+size)+" to full table");
			assertFalse(hash.add(i+size));
			assertTrue(numElements == hash.size());
		}
	}

	/**
	 * Basic add - dup Test.  This test operates on the following principle:
	 * 
	 * 1) completely fill the hash table (no collisions). Ensure that data goes to the correct
	 *    location and that size grows as expected.
	 * 2) attempt to add an existing element to the hash at each table location. Check that
	 *    add fails and that size (# of elements in the hash) is unchanged.
	 */
	@Test
	@Order(9)
	void BasicAddDup_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #15: Duplicate add behavior...");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < (size-1); i++) { 
			int numElements = hash.size();
			assertTrue(hash.add(i));
			assertTrue((numElements+1)==hash.size());
			assertEquals(i,hash.getHashAt(i, 0));
		}
		
		int numElements = hash.size();
		for (int i = 0; i < (size-1); i++) {
			System.out.println("   Attempting to add duplicate "+(i)+" to table");
			assertTrue(hash.contains(i));
			assertFalse(hash.add(i));
			assertTrue(numElements == hash.size());
		}
	}

	/**
	 * Basic contains full test. This test is checking to see that running contains()
	 * on a fulltable hash behave correctly if the hash does NOT contain the key.
	 * 1) completely fill the hash table (no collisions). Ensure that data goes to the correct
	 *    location and that size grows as expected.
	 * 2) Check for a non-existant value at each hash table location. Make sure that it returns 
	 *    false.
	 */
	@Test
	@Order(9)
	void BasicContainsFull_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #16: Checking contains failures on full table");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) { 
			int numElements = hash.size();
			assertTrue(hash.add(i));
			assertTrue((numElements+1)==hash.size());
			assertEquals(i,hash.getHashAt(i, 0));
		}
		
		int numElements = hash.size();
		assertTrue(numElements == hash.getTableSize());
		for (int i = 0; i < size; i++) {
			System.out.println("   Checking contains "+(i+size)+" to full table");
			assertFalse(hash.contains(i+size));
		}
	}

	void printHash() {
		System.out.println("Printing HashTable1 (LP):");
		for (int i =0 ; i < hash.getTableSize(); i++) {
			System.out.println("   ["+i+"] : "+hash.getHashAt(i, 0));
		}

	}

	

}
