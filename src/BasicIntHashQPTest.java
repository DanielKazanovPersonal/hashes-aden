import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
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
 * The Class BasicIntHashQPTest.
 */
@TestMethodOrder(OrderAnnotation.class)
class BasicIntHashQPTest {
	private final boolean DEBUG = false;
	private MyIntHash hash;
	private int[] qpHash = new int[31]; // this will check QP random values during the grow hash

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
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
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
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Basic Test #2: Hash Function ");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(i));
			assertEquals(i,hash.getHashAt(i, 0));
		}
		assertEquals(size,hash.getTableSize());
	}
	
	
	/**
	 * Basic hash func rand test. Generates random values for each index
	 * of the hash table with no collisions. Validates that each value is
	 * placed in the appropriate location.
	 */
	@Test
	@Order(3)
	void BasicHashFuncRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
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
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
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
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
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
	 * will collide, and should find a home using quadratic probing. Entries are checked for proper
	 * location and values
	 */
	@Test
	@Order(6)
	void BasicHashCollision_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Basic Test #6: Hash Collision handling method");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int index = 3;
		for (int i = 0; i < size/2; i++) {
			int key = i*size + index;
			System.out.println("   Before Add: Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
			System.out.println("   After Add:  Checking that hash does contain "+key);
			assertTrue(hash.contains(key));
			System.out.println("   After Add:  Checking that hash does contain "+key+" at index "+((index+i*i)%size));
			assertEquals(key,hash.getHashAt(((index+i*i)%size), 0));
		}
		if (DEBUG) printHash();
	}
	
	/**
	 * Basic hash collision rand test.  Same as test #6 - the hash is filled with randomized
	 * values that are all unique and collide. Checks to see that entries are in proper locations and
	 * order.
	 */
	@Test
	@Order(7)
	void BasicHashCollisionRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		Random random = new Random();
		System.out.println("Basic Test #7: Hash Collision handling - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		ArrayList<Integer> randInts = new ArrayList<Integer>();
		int rndIndex = random.nextInt(size); // generate the random index;
		System.out.println("   Generating key: (random base * table size) + index");
		while (randInts.size() < size/2) {
			Integer randomInt = random.nextInt(1024);
			if (!randInts.contains(randomInt)) {
				System.out.println("   i = "+randInts.size()+"  index = "+randomInt);
				randInts.add(randomInt);
			}
		}
		
		for (int i = 0; i < randInts.size(); i++) {
			int key = randInts.get(i)*size + rndIndex;
			System.out.println("   Before Add: Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
			System.out.println("   After Add:  Checking that hash does contain "+key);
			assertTrue(hash.contains(key));
			System.out.println("   After Add:  Checking that hash does contain "+key+" at index "+((i*i+rndIndex)%size));
			assertEquals(key,hash.getHashAt(((i*i+rndIndex)%size), 0));
		}
	}

	/**
	 * Basic hash Remove no collisions test. Base case where there are no collisions, so should just
	 * remove the matching key...Makes sure that the value did exist, that the Remove passed, and that the
	 * value has been removed from the hash at the expected index.
	 */
	@Test
	@Order(8)
	void BasicHashRemoveNoCollisions_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Basic Test #8: Hash Remove no collisions handling method");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) {
			assertFalse(hash.contains(i));
			assertTrue(hash.add(i));
		}
		
		int numElements = hash.size();
		for (int i = 0; i < size; i++) {
			assertTrue(hash.contains(i));
			assertEquals(i,hash.getHashAt(i, 0));
			assertTrue(hash.remove(i));
			System.out.println("   Checking that "+i+" has been properly removed from the hash");
			assertFalse(hash.contains(i));
			numElements--;
			assertEquals(numElements,hash.size());
			assertEquals(-2,hash.getHashAt(i, 0));	// -2 inicates that the value was removed. 
		}
	}

	/**
	 * Basic hash Remove with collisions A test. Fill the hash with tableSize/2 entries - all with a colliding
	 * index - this will exercise the add logic. Store the indexes of each add for content checking later. Remove
	 * the values in the order added - this will stress contains and remove. Make sure that the matching key 
	 * has been removed, but that all other keys are contained and in the correct location
	 * 
	 */
	@Test
	@Order(9)
	void BasicHashRemoveCollisionsA_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Basic Test #9: Hash Remove with collisions handling method - remove in order");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int index = 3;
		int[]  expData = new int[size];
		for (int i = 0; i < size; i++) expData[i] = -1;
		// initialize the hash 
		for (int i = 0; i < size/2; i++) {
			int key = i*size + index;
			System.out.println("   Before Add: Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
			System.out.println("   After Add:  Checking that hash does contain "+key);
			assertTrue(hash.contains(key));
			System.out.println("   After Add:  Checking that hash does contain "+key+" at index "+((index+i*i)%size));
			assertEquals(key,hash.getHashAt(((index+i*i)%size), 0));
			expData[(index+i*i)%size]=key;
		}	
		if (DEBUG) printHash();
		
		int numElements = hash.size();
		// remove entries in order added from the hash
		for (int i = 0; i < size/2; i++) {
			int key = i*size + index;
			System.out.println("   Before remove: Checking that hash contains "+key);
			assertTrue(hash.contains(key));
			assertTrue(hash.remove(key));
			System.out.println("   After remove:  Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			numElements--;
			assertEquals(numElements,hash.size());
			expData[(index+i*i)%size]=-2;
			System.out.println("   After remove:  Checking hash contents");
			for (int j = 0; j < size; j++) assertEquals(expData[j],hash.getHashAt(j, 0));
		}	
		if (DEBUG) printHash();
	}

	/**
	 * Basic hash Remove with collisions B test. Fill the hash with tableSize/2 entries - all with a colliding
	 * index - this will exercise the add logic. Store the indexes of each add for content checking later. Remove
	 * the values in reverse order added - this will stress contains and remove. Make sure that the matching key 
	 * has been removed, but that all other keys are contained and in the correct location
	 * 
	 */
	@Test
	@Order(10)
	void BasicHashRemoveCollisionsB_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Basic Test #10: Hash Remove with collisions handling method - remove in reverse order");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int index = 3;
		int[]  expData = new int[size];
		for (int i = 0; i < size; i++) expData[i] = -1;
		// initialize the hash 
		for (int i = 0; i < size/2; i++) {
			int key = i*size + index;
			System.out.println("   Before Add: Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
			System.out.println("   After Add:  Checking that hash does contain "+key);
			assertTrue(hash.contains(key));
			System.out.println("   After Add:  Checking that hash does contain "+key+" at index "+((index+i*i)%size));
			assertEquals(key,hash.getHashAt(((index+i*i)%size), 0));
			expData[(index+i*i)%size]=key;
		}	
		
		int numElements = hash.size();
		// remove entries in order added from the hash
		for (int i = (size/2)-1; i >= 0; i--) {
			int key = i*size + index;
			System.out.println("   Before remove: Checking that hash contains "+key);
			assertTrue(hash.contains(key));
			assertTrue(hash.remove(key));
			System.out.println("   After remove:  Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			numElements --;
			assertEquals(numElements,hash.size());
			expData[(index+i*i)%size]=-2;
			System.out.println("   After remove:  Checking hash contents");
			for (int j = 0; j < size; j++) assertEquals(expData[j],hash.getHashAt(j, 0));
		}	
	}

	/**
	 * Basic hash Remove Add Test. Add an entry to an index, validate that it got there, then remove it and add
	 * a different number with the same hash value - it should go into the spot that was replaced....
	 * 
	 */
	@Test
	@Order(11)
	void BasicHashRemoveAdd_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Basic Test #9: Hash Remove with collisions handling method - remove in order");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int index = 0; index < size; index ++) {		
			System.out.println("   Before Add: Checking that hash does not contain "+index);
			assertFalse(hash.contains(index));
			assertTrue(hash.add(index));
			System.out.println("   After Add:  Checking that hash does contain "+index);
			assertTrue(hash.contains(index));
			System.out.println("    Removing key: "+index);
			assertTrue(hash.remove(index));
			assertFalse(hash.contains(index));
			assertEquals(-2,hash.getHashAt(index, 0));
			System.out.println("    Adding key: "+(index+size)+" ==> Should be placed at index "+index);
			assertTrue(hash.add(index+size));
			assertTrue(hash.contains(index+size));
			assertEquals((index+size),hash.getHashAt(index, 0));	
			for (int j = 0; j < size; j++) assertTrue(-2 != hash.getHashAt(j, 0));
		}
		
	}


	/**
	 * Basic hash grow test. Tests to see that the hash grows as expected when the addition of a new value would
	 * exceed the load factor. Uses basic data from 0 to size-1
	 */
	@Test
	@Order(12)
	void BasicHashGrow_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,0.75);
		System.out.println("Basic Test #12: Hash Growth - Exceeding Load Factor ");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int checkSize = size;
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(i));
			assertEquals(i,hash.getHashAt(i, 0));
			if ((i+1)/(1.0*checkSize)>0.75) {
				System.out.println("   Exceeded load factor: "+hash.getLoad_factor()+" size="+hash.size()+"  table size = "+size);
				System.out.println("   New table size is "+hash.getTableSize());
			    assertFalse(checkSize == hash.getTableSize());
			    checkSize = hash.getTableSize();
			    for (int j = 0; j < i; j++) {
			    	assertTrue(hash.contains(j));
			    }
			}
			assertEquals(checkSize,hash.getTableSize());
		}
		assertTrue(hash.add(checkSize-1));  
	}
	
<<<<<<< HEAD
=======
	/**
	 * Basic hash func test. Tests the basic functionality of the clear QP hash function
	 * by adding keys from 0 to size -1 of the hash. Tests that entries are where 
	 * they should be. No collisions. Then tests clear to show that the full array is cleared
	 */
	@Test
	@Order(13)
	void BasicHashClearQP_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Clear Test: Quadratic Probing Hash");
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
	
>>>>>>> 2a6252ba9b6c1f197275b02404662353a3dcebfc
	private boolean canPlace(Integer randKey) {
		int base = randKey % 31;
		for (int ind = 0; ind < 31; ind++) {
			int placeInd = (base+ind*ind)%31;
			if (qpHash[placeInd]==-1) {
				qpHash[placeInd]=randKey;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Basic hash grow rand test. Similar to Test 12, but with randomize data - collisions are not prevented, and
	 * are likely to occur naturally.
	 */
	@Test
	@Order(13)
	void BasicHashGrowRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,0.75);
		Arrays.fill(qpHash, -1);
		Random random = new Random();
		System.out.println("Basic Test #13: Grow Hash Function - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		
		ArrayList<Integer> randInts = new ArrayList<Integer>();
		int rndIndex = random.nextInt(size); // generate the random index;
		System.out.println("   Generating Random Keys:");
		while (randInts.size() < size) {
			Integer randomInt = random.nextInt(256);
			if (!randInts.contains(randomInt) && canPlace(randomInt)) {
				System.out.println("   i = "+randInts.size()+"  key = "+randomInt);
				randInts.add(randomInt);
			}
		}

		int checkSize = size;
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(randInts.get(i)));
			assertTrue(hash.contains(randInts.get(i)));
			if ((i+1)/(1.0*checkSize)>0.75) {
				System.out.println("   Exceeded load factor: "+hash.getLoad_factor()+" size="+hash.size()+"  table size = "+size);
				System.out.println("   New table size is "+hash.getTableSize());
			    assertFalse(checkSize == hash.getTableSize());
			    printValidHash();
			    checkSize = hash.getTableSize();
			    for (int j = 0; j <i; j++) {
			    	System.out.println("Checking hash contents after growth - looking for key: "+randInts.get(j));    		
			    	assertTrue(hash.contains(randInts.get(j)));
			    }
			}
			assertEquals(checkSize,hash.getTableSize());
		}	
		assertEquals(checkSize,hash.getTableSize());
	}
	
	/**
	 * Basic hash grow rand test. Similar to Test 12, but with randomize data - collisions are not prevented, and
	 * are likely to occur naturally.
	 */
	@Test
	@Order(14)
	void BasicHashGrowAdd_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,0.75);
		int qpInit[] = {145,98,28,158,61,48,91,170,143,135,161,43,4,245,173,242,23,63,169,32,180,109,177,81,73,116,19,218,134,66,42};
		int qpPostExp[] = {11,31,28,24,61,48,25,36,9,1,27,43,4,44,39,41,23,63,35,32,46,42,47,14,6,49,19,17,0,66,51};
		int qpPreExp[] = {21,5,28,3,30,17,29,15,19,11,6,12,4,1,18,25,23,2,14,10,26,16,22};
		System.out.println("Basic Test #14: Grow Hash Function - checking Add during grow function");
		int size = hash.getTableSize();
		assertEquals(31,size);
		
		// load hash up to just before the growHash
		int checkSize = size;
		for (int i = 0; i < 23; i++ ) {
			System.out.println("   Adding Keys:");
			System.out.println("   i = "+i+"  key = "+qpInit[i]);
			assertTrue(hash.add(qpInit[i]));
			assertTrue(hash.contains(qpInit[i]));
			assertEquals(qpInit[i],hash.getHashAt(qpPreExp[i], 0));
		}
		printHash();
		assertEquals(checkSize,hash.getTableSize());
		
		for (int i = 23; i < qpInit.length; i++) {
			assertTrue(hash.add(qpInit[i] ));
			assertTrue(hash.contains(qpInit[i]));
			if ((i+1)/(1.0*checkSize)>0.75) {
				System.out.println("   Exceeded load factor: "+hash.getLoad_factor()+" size="+hash.size()+"  table size = "+size);
				System.out.println("   New table size is "+hash.getTableSize());
			    assertFalse(checkSize == hash.getTableSize());
			    checkSize = hash.getTableSize();
			    for (int j = 0; j < i; j++) {
			    	System.out.println("Checking hash contents after growth - looking for key: "+qpInit[j]);
			    	assertTrue(hash.contains(qpInit[j]));
			    	assertEquals(qpInit[j],hash.getHashAt(qpPostExp[j], 0));
			    }
			}
	    	assertEquals(qpInit[i],hash.getHashAt(qpPostExp[i], 0));			
			assertEquals(checkSize,hash.getTableSize());
		}	
		printValidHash();
		assertEquals(checkSize,hash.getTableSize());
	}
		
	
	/**
	 * Basic add - full Test.  This test operates on the following principle:
	 * 
	 * 1) completely fill the hash table (no collisions). Ensure that data goes to the correct
	 *    location and that size grows as expected.
	 * 2) attempt to add a new element to the hash at each table location. Check that
	 *    add does not fail and that size (# of elements in the hash) increases.
	 */
	@Test
	@Order(15)
	void BasicAddFull_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Basic Test #15: Add Failures due to full table");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) { 
			int numElements = hash.size();
			assertTrue(hash.add(i));
			assertTrue((numElements+1)==hash.size());
			assertEquals(i,hash.getHashAt(i, 0));
		}
		
		int numElements = hash.size();
		System.out.println("   Attempting to add "+(size)+" to full table");
		assertTrue(hash.add(size));
		assertTrue(numElements+1 == hash.size());
		assertTrue(67 == hash.getTableSize());
	}

	/**
	 * Basic add - Dup Test.  This test operates on the following principle:
	 * 
	 * 1) completely fill the hash table (no collisions). Ensure that data goes to the correct
	 *    location and that size grows as expected.
	 * 2) attempt to add an existing element to the hash at each table location. Check that
	 *    add fails and that size (# of elements in the hash) is unchanged.
	 */
	@Test
	@Order(16)
	void BasicAddDup_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Basic Test #16: Duplicate add behavior...");
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
	@Order(17)
	void BasicContainsFull_test() {
		hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1);
		System.out.println("Basic Test #17: Checking contains failures on full table");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) { 
			int numElements = hash.size();
			assertTrue(hash.add(i));
			assertTrue((numElements+1)==hash.size());
			assertEquals(i,hash.getHashAt(i, 0));
		}
		
		int numElements = hash.size();
		for (int i = 0; i < size; i++) {
			System.out.println("   Checking contains "+(i+size)+" to full table");
			assertFalse(hash.contains(i+size));
		}
	}

	void printHash() {
		System.out.println("Printing HashTable1 (QP):");
		for (int i =0 ; i < hash.getTableSize(); i++) {
			System.out.println("   ["+i+"] : "+hash.getHashAt(i, 0));
		}

	}
	void printValidHash() {
		System.out.println("Printing Valid HashTable1 (QP):");
		for (int i =0 ; i < hash.getTableSize(); i++) {
			if (hash.getHashAt(i, 0) != -1)
			System.out.println("   ["+i+"] : "+hash.getHashAt(i, 0));
		}

	}


	
}
