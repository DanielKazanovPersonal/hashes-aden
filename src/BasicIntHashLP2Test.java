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
 * The Class BasicIntHashLP2Test.
 */
@TestMethodOrder(OrderAnnotation.class)
class BasicIntHashLP2Test {
	private final boolean DEBUG = false;
	private final int EMPTY = -1;
	private final int REMOVED = -2;
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
	 * Basic init test. Check that every entry is initialized to EMPTY
	 */

	/**
	 * Basic hash Remove test with collisions and wraparound. This is the example that is in the
	 * foils and that we went through in class.
	 */
	@Test
	@Order(1)
	void BasicHashRemove0_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #8: Hash Remove no collisions handling method");
		int size = hash.getTableSize();
		hash.add(0);
		hash.add(30);
		hash.add(31);
		hash.add(61);
		hash.add(2);
		assertEquals(0,hash.getHashAt(0, 0));
		assertEquals(31,hash.getHashAt(1, 0));
		assertEquals(61,hash.getHashAt(2, 0));
		assertEquals(2,hash.getHashAt(3, 0));
		assertEquals(30,hash.getHashAt(30, 0));
		if (DEBUG) printHash();
		
		hash.remove(0);
		assertEquals(REMOVED,hash.getHashAt(0, 0));
		assertEquals(31,hash.getHashAt(1, 0));
		assertEquals(61,hash.getHashAt(2, 0));
		assertEquals(2,hash.getHashAt(3, 0));
		assertEquals(30,hash.getHashAt(30, 0));
		if (DEBUG) printHash();
	}
	
	/**
	 * Basic hash Remove no collisions test. Base case where there are no collisions, so should just
	 * remove the matching key...Makes sure that the value did exist, that the Remove passed, and that the
	 * value has been removed from the hash at the expected index.
	 */
	@Test
	@Order(2)
	void BasicHashRemoveNoCollisions_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #2: Hash Remove no collisions handling method");
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
			System.out.println("   Checking that "+i+" has been properly removed from the hash");
			assertFalse(hash.contains(i));
			assertEquals(REMOVED,hash.getHashAt(i, 0));	
		}
	}
	
	/**
	 * Basic hash Remove no collisions test. Base case where there are no collisions, so should just
	 * remove the matching key...However, makes sure that the value did exist, that the Remove passed, and that the
	 * value has been removed from the hash at the expected index and set = EMPTY...
	 */
	@Test
	@Order(3)
	void BasicHashRemoveNoCollisionsEmpty_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #3: Hash Remove no collisions handling method but marked as EMPTY");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) {
			assertFalse(hash.contains(i));
			assertTrue(hash.add(i));
			assertTrue(hash.contains(i));
			System.out.println("   "+i+" has been added to the hash at index "+i);
			assertTrue(hash.remove(i));
			System.out.println("   Checking that "+i+" has been properly removed from the hash");
			assertFalse(hash.contains(i));
			assertEquals(EMPTY,hash.getHashAt(i, 0));	
		}
		assertTrue(hash.size() == 0);
	}
	/**
	 * Basic hash Remove no collisions test. Base case where there are no collisions, so should just
	 * remove the matching key...However, makes sure that the value did exist, that the Remove passed, and that the
	 * value has been removed from the hash at the expected index and set = EMPTY...Then add the value back 
	 * and make sure that it went to the same entry
	 */
	@Test
	@Order(4)
	void BasicHashRemoveNoCollisionsAdd_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #4: Hash Remove no collisions handling method followed by add of same data");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) {
			assertFalse(hash.contains(i));
			assertTrue(hash.add(i));
			assertTrue(hash.contains(i));
			assertEquals(i,hash.getHashAt(i, 0));
			System.out.println("   "+i+" has been added to the hash at index "+i);
			assertTrue(hash.remove(i));
			System.out.println("   Checking that "+i+" has been properly removed from the hash");
			assertFalse(hash.contains(i));
			if ((i+1)==size) 
				assertEquals(REMOVED,hash.getHashAt(i, 0));
			else
				assertEquals(EMPTY,hash.getHashAt(i, 0));
			assertTrue(hash.add(i));
			assertTrue(hash.contains(i));
			assertEquals(i,hash.getHashAt(i, 0));			
		}
		assertTrue(hash.size() == 31);
	}	
	/**
	 * Basic hash rand Remove no collisions test. Same as #8 but with randomize data that will not collide
	 */
	@Test
	@Order(5)
	void BasicHashRandRemoveNoCollisions_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		Random random = new Random();
		System.out.println("Basic Test #5: Hash Remove no collisions handling method- randomized");
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
			int key = randInts.get(i)*size+i;
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
		}
		
		for (int i = 0; i < size; i++) {
			int key = randInts.get(i)*size+i;
			assertTrue(hash.contains(key));
			assertEquals(key,hash.getHashAt(i, 0));
			assertTrue(hash.remove(key));
			System.out.println("   Checking that "+key+" has been properly removed from the hash");
			assertFalse(hash.contains(key));
			assertEquals(REMOVED,hash.getHashAt(i, 0));	
		}
	}
	
	/**
	 * Basic hash Remove collision test. This test fills the hash with colliding indexes, and the
	 * removes them in the same order. This should leave the entire array filled with REMOVED
	 */
	@Test
	@Order(6)
	void BasicHashRemoveCollision_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #6: Hash Remove with Collision handling");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int stIndex = 3;
		for (int i = 0; i < size; i++) {
			int key = i*size + stIndex ;
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
		}
		
		int replIndex =stIndex;
		int key = stIndex;
		for (int i = 0; i < size; i++) {
			assertTrue(hash.remove(key));
			assertFalse(hash.contains(key));
			assertEquals(REMOVED,hash.getHashAt(replIndex, 0));
			System.out.println("   Successfully set contents at replIndex ["+replIndex+"] to REMOVED");
			replIndex ++;
			if (replIndex == size) replIndex = 0;
			int index = replIndex;
			for (int j = i+1; j < size; j++) {
				int expect = j*size+stIndex;
				assertTrue(hash.contains(expect));
				assertEquals(expect,hash.getHashAt(index,0));
				index++;
				if (index == size) index = 0;
			}
			key = (i+1)*size+stIndex;	
		}
		assertEquals(0, hash.size());
	}	
	
	/**
	 * Basic hash Remove collision test. This test fills the hash with colliding indexes, and the
	 * removes them in the same order. This should leave the entire array filled with REMOVED
	 */
	@Test
	@Order(7)
	void BasicHashRemoveCollisionAdd_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test #7: Hash Remove with Collision handling followed by Add");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int stIndex = 3;
		for (int i = 0; i < size; i++) {
			int key = i*size + stIndex ;
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
		}
		
		int replIndex =stIndex;
		int key = stIndex;
		for (int i = 0; i < size; i++) {
			assertTrue(hash.remove(key));
			assertFalse(hash.contains(key));
			assertEquals(REMOVED,hash.getHashAt(replIndex, 0));
			System.out.println("   Successfully set contents at replIndex ["+replIndex+"] to REMOVED");
			int index = replIndex;
			for (int j = i+1; j < size; j++) {
				index++;
				if (index == size) index = 0;
				int expect = j*size+stIndex;
				assertTrue(hash.contains(expect));
				assertEquals(expect,hash.getHashAt(index,0));
			}
			assertTrue(hash.add(key));
			assertTrue(hash.contains(key));
			assertEquals(key,hash.getHashAt(replIndex, 0));
			replIndex ++;
			if (replIndex == size) replIndex = 0;
			key = (i+1)*size+stIndex;	
		}
		assertEquals(31, hash.size());
	}	
	
	/**
	 * Basic hash Remove rand test. Same as test #4, but with randomized data. This is not a comprehensive test.
	 */
	@Test
	@Order(8)
	void BasicHashRemoveRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		Random random = new Random();
		System.out.println("Basic Test #5: Hash Remove with Collision handling - Randomized");
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
		
		int replIndex = rndIndex;
		int key = randInts.get(0)*size + rndIndex;
		for (int i = 0; i < size; i++) {
			assertTrue(hash.remove(key));
			assertFalse(hash.contains(key));
			assertEquals(REMOVED,hash.getHashAt(replIndex, 0));
			System.out.println("   Successfully set contents at index ["+replIndex+"] to REMOVED");
			replIndex++;
			if (replIndex == size) replIndex = 0;
			int index = replIndex;
			for (int j = i+1; j < size; j++) {
				int expect = randInts.get(j)*size+rndIndex;
				assertTrue(hash.contains(expect));
				assertEquals(expect,hash.getHashAt(index,0));
				index++;
				if (index == size) index = 0;
			}
			if ((i+1) < size)
				key = randInts.get(i+1)*size+rndIndex;
		}
		assertEquals(0, hash.size());
	}	
	
	/**
	 * Basic hash grow test. Tests to see that the hash grows as expected when the addition of a new value would
	 * exceed the load factor. Uses basic data from 0 to size - 1
	 */
	@Test
	@Order(9)
	void BasicHashGrow_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,0.75);
		System.out.println("Basic Test #6: Hash Growth - Exceeding Load Factor ");
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
		assertTrue(hash.add(checkSize - 1));  
	}
	
	
	/**
	 * Basic hash grow rand test. Similar to Test 6, but with randomize data - collisions are not prevented, and
	 * are likely to occur naturally.
	 */
	@Test
	@Order(10)
	void BasicHashGrowRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,0.75);
		Random random = new Random();
		System.out.println("Basic Test #7: Hash Function - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		
		ArrayList<Integer> randInts = new ArrayList<Integer>();
		int rndIndex = random.nextInt(size); // generate the random index;
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
	 * Debug hash Remove collision test.
	 * 
	 * Tests correct Remove Handling over the wrap-around....
	 * Review the Remove Scenarios covered in class
	 */
	@Test
	@Order(17)
	void DebugHashRemoveCollision_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test: Hash Remove with Collision handling - wrap around");
		int size = hash.getTableSize();
		assertEquals(31,size);

		hash.add(30);
		hash.add(0);
		hash.add(31);  // collides with 0 = should be in 1
		hash.add(61);  // collides with 30 = should be in 2
		hash.add(2);   // collides with 61 - should be in 3
		
		//hash.printHashTable1();
		assertTrue(hash.getHashAt(30,0)==30);
		assertTrue(hash.getHashAt(0,0)==0);
		assertTrue(hash.getHashAt(1,0)==31);
		assertTrue(hash.getHashAt(2,0)==61);
		assertTrue(hash.getHashAt(3,0)==2);
		hash.remove(0);
		//hash.printHashTable1();
		assertFalse(hash.contains(0));
		assertTrue(hash.contains(30));
		assertTrue(hash.contains(2));
		assertTrue(hash.contains(31));
		assertTrue(hash.contains(61));
		hash.remove(30);
		//hash.printHashTable1();
		assertFalse(hash.contains(30));
		assertTrue(hash.contains(2));
		assertTrue(hash.contains(31));
		assertTrue(hash.contains(61));
		
	}	
	
	/**
	 * Debug hash Remove collision 2 test.
	 * Variant of test 17 - testing correct Remove when data wraps
	 */
	@Test
	@Order(18)
	void DebugHashRemoveCollision2_test() {
		hash = new MyIntHash(MyIntHash.MODE.Linear,1.1);
		System.out.println("Basic Test: Hash Remove with Collision handling - wrap around");
		int size = hash.getTableSize();
		assertEquals(31,size);

		hash.add(29);  // in 29
		hash.add(60);  // in 30, but should be 29
		hash.add(30);  // in 0, but should be 30...
		hash.add(31);  // collides with 0 = should be in 1
		hash.add(61);  // collides with 29 = should be in 2
		hash.add(2);   // collides with 61 - should be in 3
		
		//hash.printHashTable1();
		assertTrue(hash.getHashAt(29,0)==29);
		assertTrue(hash.getHashAt(30,0)==60);
		assertTrue(hash.getHashAt(0,0)==30);
		assertTrue(hash.getHashAt(1,0)==31);
		assertTrue(hash.getHashAt(2,0)==61);
		assertTrue(hash.getHashAt(3,0)==2);
		hash.remove(29);
		//hash.printHashTable1();
		assertFalse(hash.contains(0));
		assertTrue(hash.contains(30));
		assertTrue(hash.contains(2));
		assertTrue(hash.contains(31));
		assertTrue(hash.contains(61));
		hash.remove(30);
		//hash.printHashTable1();
		assertFalse(hash.contains(30));
		assertTrue(hash.contains(2));
		assertTrue(hash.contains(31));
		assertTrue(hash.contains(61));
		
	}
	
	void printHash() {
		System.out.println("Printing HashTable1 (LP):");
		for (int i =0 ; i < hash.getTableSize(); i++) {
			System.out.println("   ["+i+"] : "+hash.getHashAt(i, 0));
		}

	}

}
