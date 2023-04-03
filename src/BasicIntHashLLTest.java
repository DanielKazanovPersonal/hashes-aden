import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;


import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicIntHashLPTest.
 */
@TestMethodOrder(OrderAnnotation.class)
class BasicIntHashLLTest {
	private MyIntHash hash;
	private final boolean DEBUG = false;
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
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
		System.out.println("Basic Test #1: Hash Table Initialization");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) 
			assertNull(hash.getHashAt(i, 0));
	}

	/**
	 * Basic hash func test. Tests the basic functionality of the hash function
	 * by adding keys from 0 to size -1 of the hash. Tests that entries are where 
	 * they should be. No collisions.
	 */
	@Test
	@Order(2)
	void BasicHashFunc_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
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
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
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
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
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
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
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
	 * will collide, and should find a home in the linked list at entry 3. Entries are checked for proper
	 * location and values
	 */
	@Test
	@Order(6)
	void BasicHashCollision_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
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
			System.out.println("   After Add:  Checking that hash does contain "+key+" at index "+index+" LL-index="+i);
			assertEquals(key,hash.getHashAt(index, i));
		}
	}
	
	/**
	 * Basic hash collision rand test.  Same as test #6 - the hash is filled with randomized
	 * values that are all unique and collide. Checks to see that entries are in proper locations and
	 * order. For Linked List, this will be at the random index, and then at the offset within the
	 * linked list based upon time of insertion order.
	 */
	@Test
	@Order(7)
	void BasicHashCollisionRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
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
			System.out.println("   After Add:  Checking that hash does contain "+key+" at index "+rndIndex+" LL-index="+i);
			assertEquals(key,hash.getHashAt(rndIndex, i));
		}
	}

	/**
	 * Basic hash Remove no collisions test. Base case where there are no collisions, so should just
	 * remove the matching key...Makes sure that the value did exist, that the Remove passed, and that the
	 * value has been removed from the hash at the expected index.
	 * For Linked Lists, make sure that the hashTable index has been reset to null after Remove
	 */
	@Test
	@Order(8)
	void BasicHashRemoveNoCollisions_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
		System.out.println("Basic Test #8: Hash Remove no collisions handling method");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) {
			assertFalse(hash.contains(i));
			assertTrue(hash.add(i));
		}
		
		for (int i = 0; i < size; i++) {
			assertTrue(hash.contains(i));
			assertEquals(i,hash.getHashAt(i, 0));
			assertTrue(hash.remove(i));
			System.out.println("   Checking that "+i+" has been properly removeed from the hash");
			assertFalse(hash.contains(i));
			assertNull(hash.getHashAt(i, 0));	
		}
	}
	
	/**
	 * Basic hash rand Remove no collisions test. Same as #8 but with randomize data that will not collide
	 * For Linked Lists, make sure that the hashTable index has been reset to null after Remove
	 */
	@Test
	@Order(9)
	void BasicHashRandRemoveNoCollisions_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
		Random random = new Random();
		System.out.println("Basic Test #9: Hash Remove no collisions handling method- randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		ArrayList<Integer> randInts = new ArrayList<Integer>();
		System.out.println("   Generating key: (random base * table size) + index");
		while (randInts.size() < size) {
			Integer randomInt = random.nextInt(1024);
			if (!randInts.contains(randomInt)) {
				System.out.println("   i = "+randInts.size()+"  index = "+randomInt);
				randInts.add(randomInt);
			}
		}

		for (int i = 0; i < size; i++) {
			int key = randInts.get(i)*size+i;
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
		}

		if (DEBUG) printHash();		
		for (int i = 0; i < size; i++) {
			int key = randInts.get(i)*size+i;
			assertTrue(hash.contains(key));
			assertEquals(key,hash.getHashAt(i, 0));
			assertTrue(hash.remove(key));
			System.out.println("   Checking that "+key+" has been properly removeed from the hash");
			assertFalse(hash.contains(key));
			assertNull(hash.getHashAt(i, 0));	
		}
	}
	
	/**
	 * Basic hash Remove collision test. This test fills the hash with colliding indexes, and the
	 * removes them in the same order. Checks are made to ensure that the expected data is removeed, and
	 * any replacement data is correctly moved, and it's prior index cleared. This is NOT a comprehensive test.
	 * For LinkedList, make sure that the LinkedList is shrinking by 1 and that the element has been removed from the list
	 * When all elements are removed, make sure that the LinkedList pointer at stIndex is set to null
	 */
	@Test
	@Order(10)
	void BasicHashRemoveCollision_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
		System.out.println("Basic Test #11: Hash Remove with Collision handling");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int stIndex = 3;
		for (int i = 0; i < size; i++) {
			int key = i*size + stIndex ;
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
			assertTrue(key == hash.getHashAt(stIndex, i));
		}
		
		for (int i = 0; i < size; i++) {
			int key = i*size+stIndex;
			assertTrue(hash.remove(key));
			assertFalse(hash.contains(key));
			if ((i+1) == size) 
				assertNull(hash.getHashAt(stIndex, 0));
			else {
				int keyAt0 = hash.getHashAt(stIndex,0);
				System.out.println("  After Remove: Key at index 0 = "+keyAt0+" i = "+i);
				assertTrue(hash.getHashAt(stIndex, 0) == (size*(i+1)+stIndex));
			}
			System.out.println("   Successfully removeed key "+key+" from hash at index 3");
		}
	}	
	
	/**
	 * Basic hash Remove rand test. Same as test #10, but with randomized data. This is not a comprehensive test.
	 * For LinkedList, make sure that the LinkedList is shrinking by 1 and that the element has been removed from the list
	 * When all elements are removed, make sure that the LinkedList pointer at rndIndex is set to null
	 */
	@Test
	@Order(11)
	void BasicHashRemoveRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
		Random random = new Random();
		System.out.println("Basic Test #11: Hash Remove with Collision handling - Randomized");
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
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
		}
		
		while (randInts.size() > 0) {
			int rndOffset = random.nextInt(randInts.size());
			int key = randInts.get(rndOffset)*size+rndIndex;
			assertTrue(hash.remove(key));
			assertFalse(hash.contains(key));
			if (randInts.size() > 1)
				assertTrue(hash.getHashAt(rndIndex, randInts.size()) == -1);
			else 
				assertNull(hash.getHashAt(rndIndex, randInts.size()));
			randInts.remove(rndOffset);
			if (randInts.size() != 0)
				assertTrue(hash.getHashAt(rndIndex, (randInts.size()-1)) != -1);
			else
				assertNull(hash.getHashAt(rndIndex, 0));
			System.out.println("   Successfully removed key "+key+" from hash at index = "+rndIndex);
			System.out.println("   LL Size at index is now "+randInts.size());
		}
	}	
	
	/**
	 * Basic hash grow test. Tests to see that the hash grows as expected when the addition of a new value would
	 * exceed the load factor. Uses basic data from 0 to size-1
	 */
	@Test
	@Order(12)
	void BasicHashGrow_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,0.75);
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
	
	
	/**
	 * Basic hash grow rand test. Similar to Test 12, but with randomize data - collisions are not prevented, and
	 * are likely to occur naturally.
	 */
	@Test
	@Order(13)
	void BasicHashGrowRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,0.75);
		Random random = new Random();
		System.out.println("Basic Test #3: Hash Function - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		
		ArrayList<Integer> randInts = new ArrayList<Integer>();
		System.out.println("   Generating key: (random base * table size) + index");
		while (randInts.size() < size) {
			Integer randomInt = random.nextInt(256);
			if (!randInts.contains(randomInt)) {
				System.out.println("   i = "+randInts.size()+"  index = "+randomInt);
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
			    checkSize = hash.getTableSize();
			    for (int j = 0; j <i; j++) {
			    	assertTrue(hash.contains(randInts.get(j)));
			    }
			}
			assertEquals(checkSize,hash.getTableSize());
		}	
	}

	/**
	 * Basic add - full Test.  This test operates on the following principle:
	 * 
	 * 1) completely fill the hash table (no collisions). Ensure that data goes to the correct
	 *    location and that size grows as expected.
	 * 2) attempt to add a new element to the hash at each table location. Check that
	 *    add does NOT fail and that size (# of elements in the hash) is increases.
	 */
	@Test
	@Order(14)
	void BasicAddFull_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,2.1);
		System.out.println("Basic Test #14: Add Does not fail due to full table");
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
			assertTrue(hash.add(i+size));
			assertTrue((++numElements) == hash.size());
		}
		if (DEBUG) printHash();
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
	@Order(15)
	void BasicAddDup_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
		System.out.println("Basic Test #15: Duplicate add behavior...");
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
	 *   
	 * Note that for the LL Table, this really does not do anything more than the original contains
	 * tests.
	 */
	@Test
	@Order(16)
	void BasicContainsFull_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
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
		for (int i = 0; i < size; i++) {
			System.out.println("   Checking contains "+(i+size)+" to full table");
			assertFalse(hash.contains(i+size));
		}
	}
	
	/**
	 * Basic hash func test. Tests the basic functionality of the clear LL hash function
	 * by adding keys from 0 to size -1 of the hash. Tests that entries are where 
	 * they should be. No collisions. Then tests clear to show that the full array is cleared
	 */
	@Test
	@Order(17)
	void BasicHashClearLL_test() {
		hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1);
		System.out.println("Clear Test: LinkedList Hash");
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
			assertEquals(null,hash.getHashAt(i, 0));
		}
	}


	void printHash() {
		System.out.println("Printing HashTable LinkedList:");
		for (int i =0 ; i < hash.getTableSize(); i++) {
			if (hash.getHashAt(i, 0) == null)
				System.out.println("   ["+i+"] : null");
			else {
				int j = 0;
				System.out.print("   ["+i+"] : ");
				int key;
				while ((key = hash.getHashAt(i, j))!=-1) {
					System.out.print(key+" ");
					if (hash.getHashAt(i,j+1) != -1) 
						System.out.print("==> ");
					j++;
				}
				System.out.println("");
			}
		}

	}

}
