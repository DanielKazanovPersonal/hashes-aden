import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class BasicIntHashCuckooTest {
	private final boolean DEBUG = false;
	MyIntHash hash;

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
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		System.out.println("Basic Test #1: Hash Table Initialization");
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int i = 0; i < size; i++) {
			assertEquals(-1,hash.getHashAt(i, 0));
			assertEquals(-1,hash.getHashAt(i, 1));	
		}
	}

	/**
	 * Basic hash func test. Tests the basic functionality of the hash function
	 * by adding keys from 0 to size -1 of the hash. Tests that entries are where 
	 * they should be. No collisions, but removal of existing entries MUST work...
	 *
	 * There are two keys in this test: 
	 * - one is the index (i) itself
	 * - the second is a key generated and stored in hash2data[i] such that 
	 * i = hashFx(hash2data[i])=hashFx2(hash2data[i])
	 * 
	 * The test works by first adding the key from hash2data[i] and then i
	 */
	@Test
	@Order(2)
	void BasicHashFunc_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		System.out.println("Basic Test #2: Hash Function ");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] hash2data = new int[size];
		for (int i = 0; i < size; i++) {
			hash2data[i] = (i == 0) ? 31*31 : 31*i+i;
		}
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(hash2data[i]));
			assertEquals(hash2data[i],hash.getHashAt(i, 0));
			assertEquals(-1, hash.getHashAt(i, 1));
			assertTrue(hash.add(i));
			assertEquals(i, hash.getHashAt(i, 0));
			assertEquals(hash2data[i],hash.getHashAt(i, 1));
		}	
		if (DEBUG) printHash();
		assertEquals(size,hash.getTableSize());
	}
	
	
	/**
	 * Basic hash func rand test. Generates random values for each index
	 * of the hash table with no collisions. Validates that each value is
	 * placed in the appropriate location. Generates a random key (rndBasePlus_Index[i])
	 * that is guaranteed to not collide, and then a second key (rndHash2Data[i]) such
	 * that i = hashFx(rndBasePlus_index[i]) = hashFx(rndHash2Data[i]) = hashFx2(rndHash2Data[i]);
	 * 
	 * As before, the test first adds rndHash2Data[i] then rndBasePlus_Index[i] for each index.
	 * This will cause eviction from first table and placement into second table...
	 * 
	 */
	@Test
	@Order(3)
	void BasicHashFuncRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		Random random = new Random();
		System.out.println("Basic Test #3: Hash Function - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] rndBasePlusIndex = new int[size];
		int[] rndHash2Data = new int[size];
		
		System.out.println("   Generating key: (random base * table size) + index");
		for (int i = 0; i < size; i++ ) {
			int rndOff = random.nextInt(128)+1;
			rndBasePlusIndex[i] = rndOff*size+i;
			int rndBaseDiv = (rndBasePlusIndex[i]/size)%size;
			if (rndBaseDiv != i) {
				rndHash2Data[i] = size*(size-rndBaseDiv+i)+rndBasePlusIndex[i];
			} else {
				rndHash2Data[i] = rndBasePlusIndex[i]+size*size;
			}
			System.out.println("   Index = "+i+"  key = "+rndBasePlusIndex[i]+"   hash1 index = "+(rndBasePlusIndex[i]%size));
			System.out.println("   Index = "+i+"  key = "+rndHash2Data[i]+    "   hash2 index = "+((rndHash2Data[i]/size)%size));
		}
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(rndHash2Data[i]));
			assertEquals(rndHash2Data[i],hash.getHashAt(i, 0));
			assertEquals(-1,hash.getHashAt(i, 1));
			assertTrue(hash.add(rndBasePlusIndex[i]));
			assertEquals(rndBasePlusIndex[i],hash.getHashAt(i, 0));
			assertEquals(rndHash2Data[i],hash.getHashAt(i, 1));
		}
		if (DEBUG) printHash();
		assertEquals(size,hash.getTableSize());
	}
	
	/**
	 * Basic hash contains test. Checks that the contains function works properly, demonstrating
	 * that:
	 * 		entry does not exist before being added
	 * 		entry exists after being added
	 *  	entry is in the correct location of the hash
	 *  
	 *  No collisions in this test. Reuses the same algorithm for adding data as in BasicHashFunc_Test
	 */
	@Test
	@Order(4)
	void BasicHashContains_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		System.out.println("Basic Test #4: Hash contains method");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] hash2data = new int[size];
		for (int i = 0; i < size; i++) {
			hash2data[i] = (i == 0) ? 31*31 : 31*i+i;
		}
		for (int i = 0; i < size; i++) {
			System.out.println("   Before Add: Checking that hash does not contain "+hash2data[i]+" or "+i);			
			assertFalse(hash.contains(hash2data[i]));
			assertFalse(hash.contains(i));
			assertTrue(hash.add(hash2data[i]));
			System.out.println("   After First Add:  Checking that hash does contain "+hash2data[i]+" but not "+i);			
			assertTrue(hash.contains(hash2data[i]));
			assertFalse(hash.contains(i));
			assertEquals(hash2data[i],hash.getHashAt(i, 0));
			assertEquals(-1, hash.getHashAt(i, 1));
			assertTrue(hash.add(i));
			System.out.println("   After Second Add:  Checking that hash does contain "+hash2data[i]+" and "+i+" at index "+i);			
			assertTrue(hash.contains(i));
			assertTrue(hash.contains(hash2data[i]));
			assertEquals(i, hash.getHashAt(i, 0));
			assertEquals(hash2data[i],hash.getHashAt(i, 1));
		}	
		if (DEBUG) printHash();
		assertEquals(size,hash.getTableSize());
	}
	
	
	/**
	 * Basic hash contains rand test. Same concept as test #4, but with random data that is
	 * guaranteed to avoid collisions, using algorithm from test #3
	 */
	@Test
	@Order(5)
	void BasicHashContainsRand_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		Random random = new Random();
		System.out.println("Basic Test #3: Hash Function - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] rndBasePlusIndex = new int[size];
		int[] rndHash2Data = new int[size];
		
		System.out.println("   Generating key: (random base * table size) + index");
		for (int i = 0; i < size; i++ ) {
			int rndOff = random.nextInt(128)+1;
			rndBasePlusIndex[i] = rndOff*size+i;
			int rndBaseDiv = (rndBasePlusIndex[i]/size)%size;
			if (rndBaseDiv != i) {
				rndHash2Data[i] = size*(size-rndBaseDiv+i)+rndBasePlusIndex[i];
			} else {
				rndHash2Data[i] = rndBasePlusIndex[i]+size*size;
			}
			System.out.println("   Index = "+i+"  key = "+rndBasePlusIndex[i]+"   hash1 index = "+(rndBasePlusIndex[i]%size));
			System.out.println("   Index = "+i+"  key = "+rndHash2Data[i]+    "   hash2 index = "+((rndHash2Data[i]/size)%size));
		}
		for (int i = 0; i < size; i++) {
			System.out.println("   Before Add: Checking that hash does not contain "+rndHash2Data[i]+" or "+rndBasePlusIndex[i]);			
			assertFalse(hash.contains(rndHash2Data[i]));
			assertFalse(hash.contains(rndBasePlusIndex[i]));
			assertTrue(hash.add(rndHash2Data[i]));
			System.out.println("   After First Add:  Checking that hash does contain "+rndHash2Data[i]+" but not "+rndBasePlusIndex[i]);			
			assertTrue(hash.contains(rndHash2Data[i]));
			assertFalse(hash.contains(rndBasePlusIndex[i]));
			assertEquals(rndHash2Data[i],hash.getHashAt(i, 0));
			assertEquals(-1, hash.getHashAt(i, 1));
			assertTrue(hash.add(rndBasePlusIndex[i]));
			System.out.println("   After Second Add:  Checking that hash does contain "+rndHash2Data[i]+" and "+rndBasePlusIndex[i]+" at index "+i);			
			assertTrue(hash.contains(rndBasePlusIndex[i]));
			assertTrue(hash.contains(rndHash2Data[i]));
			assertEquals(rndBasePlusIndex[i], hash.getHashAt(i, 0));
			assertEquals(rndHash2Data[i],hash.getHashAt(i, 1));
		}	
		if (DEBUG) printHash();
	}
	
	/**
	 * Basic hash collision test. Picking index 3 as default, fills the hash sequentially by
	 * adding the key (initial value 3), and then adding size to generate subsequent keys. All
	 * will collide, and should find a home using the second hash table. Entries are checked for proper
	 * location and values. The primary hash table should have a single entry at index 3 and then 
	 * all entries filled in secondary hash table.
	 */
	@Test
	@Order(6)
	void BasicHashCollision_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		System.out.println("Basic Test #6: Hash Collision handling method");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int index = 3;
		for (int i = 0; i <= size; i++) {
			int key = i*size + index;
			System.out.println("   Before Add: Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
			System.out.println("   After Add:  Checking that hash does contain "+key);
			assertTrue(hash.contains(key));
			if (i > 0 ) {
				System.out.println("   After Add:  Checking that sTable does contain "+key+" at index "+(i-1));
				assertEquals((i-1)*size+index,hash.getHashAt(i-1, 1));
			}
			System.out.println("   After Add:  Checking that pTable does contain "+key+" at index "+index);
			assertEquals(key,hash.getHashAt(index, 0));
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
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		Random random = new Random();
		System.out.println("Basic Test #7: Hash Collision handling - Randomized");
		int size = hash.getTableSize();
		assertEquals(31,size);
		ArrayList<Integer> randInts = new ArrayList<Integer>();
		int rndIndex = random.nextInt(size); // generate the random index;
		System.out.println("Collisions will occur at index: "+rndIndex);

		
		System.out.println("   Generating key: (random base * table size) + index");
		while (randInts.size() <= size) {
			Integer randomInt = random.nextInt(256);
			if (!randInts.contains(randomInt)) {
				System.out.println("   i = "+randInts.size()+"  index = "+randomInt);
				randInts.add(randomInt);
			}
		}
		
		int key;
		int old_key=-1;
		for (int i = 0; i <= size; i++) {
			int rndBaseDiv = (randInts.get(i)/size)%size;
			key = (i)*size + randInts.get(i)*size*size+rndIndex;
			System.out.println("   Before Add: Checking that hash does not contain "+key);
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
			System.out.println("   After Add:  Checking that hash does contain "+key);
			assertTrue(hash.contains(key));
			if (i > 0 ) {
				System.out.println("   After Add:  Checking that sTable does contain "+old_key+" at index "+(i-1));
				assertEquals(old_key,hash.getHashAt(i-1, 1));
			}
			System.out.println("   After Add:  Checking that pTable does contain "+key+" at index "+rndIndex);
			assertEquals(key,hash.getHashAt(rndIndex, 0));
			old_key = key;
		}
		if (DEBUG) printHash();
	}

	/**
	 * Basic hash removal no collisions test. Base case where there are no collisions, so should just
	 * remove the matching key...Makes sure that the value did exist, that the removal passed, and that the
	 * value has been removed from the hash at the expected index.
	 */
	@Test
	@Order(8)
	void BasicHashRemoveNoCollisions_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		System.out.println("Basic Test #8: Hash Remove no collisions handling method");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] hash2data = new int[size];
		for (int i = 0; i < size; i++) {
			hash2data[i] = (i == 0) ? 31*31 : 31*i+i;
		}
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(hash2data[i]));
			assertTrue(hash.add(i));
			assertEquals(i, hash.getHashAt(i, 0));
			assertEquals(hash2data[i],hash.getHashAt(i, 1));
		}	
		if (DEBUG) printHash();
		assertEquals(size,hash.getTableSize());

		for (int i = 0; i < size; i++) {
			assertTrue(hash.contains(i));
			assertEquals(i,hash.getHashAt(i, 0));
			assertTrue(hash.remove(i));
			System.out.println("   Checking that "+i+" has been properly removed from the hash");
			assertFalse(hash.contains(i));
			assertEquals(-1,hash.getHashAt(i, 0));

			assertTrue(hash.contains(hash2data[i]));
			assertEquals(hash2data[i],hash.getHashAt(i, 1));
			assertTrue(hash.remove(hash2data[i]));
			System.out.println("   Checking that "+hash2data[i]+" has been properly removed from the hash");
			assertFalse(hash.contains(hash2data[i]));
			assertEquals(-1,hash.getHashAt(i, 1));	
		}
	}
	
	/**
	 * Basic hash rand removal no collisions test. Same as #8 but with randomize data that will not collide
	 */
	@Test
	@Order(9)
	void BasicHashRandRemoveNoCollisions_test() {
		System.out.println("Basic Test #9: Hash Remove no collisions handling method- randomized");
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		Random random = new Random();
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] rndBasePlusIndex = new int[size];
		int[] rndHash2Data = new int[size];
		int key;
		
		System.out.println("   Generating key: (random base * table size) + index");
		for (int i = 0; i < size; i++ ) {
			int rndOff = random.nextInt(128)+1;
			rndBasePlusIndex[i] = rndOff*size+i;
			int rndBaseDiv = (rndBasePlusIndex[i]/size)%size;
			if (rndBaseDiv != i) {
				rndHash2Data[i] = size*(size-rndBaseDiv+i)+rndBasePlusIndex[i];
			} else {
				rndHash2Data[i] = rndBasePlusIndex[i]+size*size;
			}
			System.out.println("   Index = "+i+"  key = "+rndBasePlusIndex[i]+"   hash1 index = "+(rndBasePlusIndex[i]%size));
			System.out.println("   Index = "+i+"  key = "+rndHash2Data[i]+    "   hash2 index = "+((rndHash2Data[i]/size)%size));
		}
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(rndHash2Data[i]));
			assertTrue(hash.add(rndBasePlusIndex[i]));
		}
		if (DEBUG) printHash();
		assertEquals(size,hash.getTableSize());

		for (int i = 0; i < size; i++) {
			key = rndBasePlusIndex[i];
			assertTrue(hash.contains(key));
			assertEquals(key,hash.getHashAt(i, 0));
			assertTrue(hash.remove(key));
			System.out.println("   Checking that "+key+" has been properly removed from the hash");
			assertFalse(hash.contains(key));
			assertEquals(-1,hash.getHashAt(i, 0));	

			key = rndHash2Data[i];
			assertTrue(hash.contains(key));
			assertEquals(key,hash.getHashAt(i, 1));
			assertTrue(hash.remove(key));
			System.out.println("   Checking that "+key+" has been properly removed from the hash");
			assertFalse(hash.contains(key));
			assertEquals(-1,hash.getHashAt(i, 1));	
		}

	}
	
	/**
	 * Basic hash removal collision test. This test fills the hash with colliding indexes, and the
	 * removes them in the same order. Checks are made to ensure that the expected data is removed, and
	 * any replacement data is correctly moved, and it's prior index cleared. This is NOT a comprehensive test.
	 */
	@Test
	@Order(10)
	void BasicHashRemoveCollision_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		System.out.println("Basic Test #10: Hash Remove with Collision handling");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int index = 3;
		int key;
		for (int i = 0; i <= size; i++) {
			key = i*size + index;
			assertFalse(hash.contains(key));
			assertTrue(hash.add(key));
		}
		if (DEBUG) printHash();

		for (int i = 0; i < size; i++) {
			key = i*size +index;
			assertTrue(hash.contains(key));
			assertTrue(hash.remove(key));
			assertFalse(hash.contains(key));
			assertEquals(-1,hash.getHashAt(i,1));
		}
		
		key = size*size+index;
		assertTrue(hash.contains(key));
		assertTrue(hash.remove(key));
		assertFalse(hash.contains(key));
		assertEquals(-1,hash.getHashAt(index,0));
		if (DEBUG) printHash();

	}	
	
	/**
	 * Basic hash remove rand test. Same as test #10, but with randomized data. This is not a comprehensive test.
	 */
	@Test
	@Order(11)
	void BasicHashRemoveRand_test() {
		System.out.println("Basic Test #11: Hash Remove with Collision handling - Randomized");
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		Random random = new Random();
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] rndBasePlusIndex = new int[size];
		int[] rndHash2Data = new int[size];
		int key;
		
		System.out.println("   Generating key: (random base * table size) + index");
		for (int i = 0; i < size; i++ ) {
			int rndOff = random.nextInt(128)+1;
			rndBasePlusIndex[i] = rndOff*size+i;
			int rndBaseDiv = (rndBasePlusIndex[i]/size)%size;
			if (rndBaseDiv != i) {
				rndHash2Data[i] = size*(size-rndBaseDiv+i)+rndBasePlusIndex[i];
			} else {
				rndHash2Data[i] = rndBasePlusIndex[i]+size*size;
			}
			System.out.println("   Index = "+i+"  key = "+rndBasePlusIndex[i]+"   hash1 index = "+(rndBasePlusIndex[i]%size));
			System.out.println("   Index = "+i+"  key = "+rndHash2Data[i]+    "   hash2 index = "+((rndHash2Data[i]/size)%size));
		}
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(rndHash2Data[i]));
			assertTrue(hash.add(rndBasePlusIndex[i]));
		}
		if (DEBUG) printHash();
		assertEquals(size,hash.getTableSize());
		
		for (int i = 0; i < size; i++) {
			key = rndBasePlusIndex[i];
			assertTrue(hash.contains(key));
			assertEquals(key,hash.getHashAt(i, 0));
			assertTrue(hash.remove(key));
			System.out.println("   Checking that "+key+" has been properly removed from the hash");
			assertFalse(hash.contains(key));
			assertEquals(-1,hash.getHashAt(i, 0));	

			key = rndHash2Data[i];
			assertTrue(hash.contains(key));
			assertEquals(key,hash.getHashAt(i, 1));
			assertTrue(hash.remove(key));
			System.out.println("   Checking that "+key+" has been properly removed from the hash");
			assertFalse(hash.contains(key));
			assertEquals(-1,hash.getHashAt(i, 1));	
		}
	}	
	
	/**
	 * Basic hash grow test. Tests to see that the hash grows as expected when the addition of a new value would
	 * exceed the load factor. Uses basic data from 0 to size-1 and remember that tableSize needs to be doubled...
	 */
	@Test
	@Order(12)
	void BasicHashGrow_test() {
		System.out.println("Basic Test #12: Hash Growth - Exceeding Load Factor ");
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,0.75);
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] hash2data = new int[size];
		for (int i = 0; i < size; i++) {
			hash2data[i] = (i == 0) ? 31*31 : 31*i+i;
		}
		int checkSize = size;
		
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(hash2data[i]));
			if ((hash.size()/(2.0*checkSize))>0.75) {
				System.out.println("   Exceeded load factor: "+hash.getLoad_factor()+" size="+hash.size()+"  table size = "+size);
				System.out.println("   New table size is "+hash.getTableSize());
			    assertFalse(checkSize == hash.getTableSize());
			    checkSize = hash.getTableSize();
			    for (int j = 0; j <i; j++) {
			    	assertTrue(hash.contains(j));
			    	assertTrue(hash.contains(hash2data[j]));
			    }
			}
		    assertTrue(hash.contains(hash2data[i]));
			assertTrue(hash.add(i));
		    assertTrue(hash.contains(i));
		}	
		if (DEBUG) printHash();
		assertEquals(checkSize,hash.getTableSize());
	}
	
	/**
	 * Hash grow test due to loop detection. Tests to see that the hash grows as expected when loops are 
	 * detected in the Cuckoo hash.. Uses the new constructor to help simplify testing
	 */
	@Test
	@Order(13)
	void BasicLoopHashGrow_test() {
		System.out.println("Basic Test #13: Hash Growth - Detected Loop ");
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,0.75);
		int size = hash.getTableSize();
		assertEquals(31,size);

		// add some specific entries in 0 that will all evict to location 0 in 2nd hash
		hash.add(0);
		hash.add(size*size);
		assertEquals(31,size);
		hash.add(size*size*size);  // this should grow the array
		int newSize = hash.getTableSize();
		assertNotEquals(size,newSize);
		assertEquals(1031,newSize);
		assertEquals(0,hash.getHashAt(0, 0));
		assertEquals((size*size), hash.getHashAt(((size*size)%newSize),0));
		assertEquals((size*size*size), hash.getHashAt(((size*size*size)%newSize),0));
		
	}
	

	@Test
	@Order(14)
	void BasicLoopHashGrowAllIndex_test() {
		System.out.println("Basic Test #14: Hash Growth - Detected Loop at non-zero RndIndex");
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,0.75);
		int size = hash.getTableSize();
		assertEquals(31,size);
		for (int index = 1; index < size; index++) {
			System.out.println("Forcing loop at index: "+index);
			int key1 = index*(size+1);
			int key2 = index*size*size+index*(size+1);
			int key3 = index*size*size*size + index*(size+1);
			System.out.println("   key1 = "+key1);
			System.out.println("   key2 = "+key2);
			System.out.println("   key3 = "+key3);

			// add some specific entries in 0 that will all evict to location 0 in 2nd hash
			hash.add(key1);
			hash.add(key2);
			assertEquals(31,size);
			hash.add(key3);  // this should grow the array
			int newSize = hash.getTableSize();
			assertNotEquals(size,newSize);
			assertEquals(1031,newSize);
			if (DEBUG) printHash();
			assertEquals(key1,hash.getHashAt(key1%newSize, 0));
			assertEquals(key2, hash.getHashAt((key2%newSize),0));
			assertEquals(key3, hash.getHashAt((key3%newSize),0));

			// clear everything and start over...
			hash = new MyIntHash(MyIntHash.MODE.Cuckoo,0.75);
			assertEquals(size,hash.getTableSize());
		}
		
	}	
	
	/**
	 * Basic add - dup Test.  This test operates on the following principle:
	 * 
	 * 1) completely fill the hash table (no collisions). Ensure that data goes to the correct
	 *    location and that size grows as expected. Use algorithm from test 2 above
	 * 2) attempt to add a duplicate element to the hash at each table location. Check that
	 *    add fails and that size (# of elements in the hash) is unchanged.
	 */
	@Test
	@Order(15)
	void BasicAddDup_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		System.out.println("Basic Test #15: Duplicate add behavior...");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] hash2data = new int[size];
		for (int i = 0; i < size; i++) {
			hash2data[i] = (i == 0) ? 31*31 : 31*i+i;
		}
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(hash2data[i]));
			assertTrue(hash.add(i));
		}	
		if (DEBUG) printHash();
		assertEquals(size,hash.getTableSize());
		int numElements = hash.size();
		for (int i = 0; i < size; i++) {
			System.out.println("   Attempting to add duplicate "+(i)+" to table");
			assertTrue(hash.contains(i));
			assertFalse(hash.add(i));
			assertTrue(numElements == hash.size());
			System.out.println("   Attempting to add duplicate "+hash2data[i]+" to table");
			assertTrue(hash.contains(hash2data[i]));
			assertFalse(hash.add(hash2data[i]));
			assertTrue(numElements == hash.size());
		}
	}

	/**
	 * Basic contains full test. This test is checking to see that running contains()
	 * on a fulltable hash behaves correctly if the hash does NOT contain the key.
	 * 1) completely fill the hash table (no collisions). Ensure that data goes to the correct
	 *    location and that size grows as expected. Use algorithm from test 2...
	 * 2) Check for a non-existent value at each hash table location. Make sure that it returns 
	 *    false.
	 */
	@Test
	@Order(16)
	void BasicContainsFull_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		System.out.println("Basic Test #16: Contains Full behavior...");
		int size = hash.getTableSize();
		assertEquals(31,size);
		int[] hash2data = new int[size];
		for (int i = 0; i < size; i++) {
			hash2data[i] = (i == 0) ? 31*31 : 31*i+i;
		}
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(hash2data[i]));
			assertTrue(hash.add(i));
		}	
		if (DEBUG) printHash();
		assertEquals(size,hash.getTableSize());
		
		int numElements = hash.size();
		for (int i = 0; i < size; i++) {
			System.out.println("   Checking contains "+(i+size+2)+" to full table");
			assertFalse(hash.contains(i+size+2));
			System.out.println("   Checking contains "+(hash2data[i]+3)+" to full table");
			assertFalse(hash.contains(hash2data[i]+3));
		}
	}

	/**
	 * Basic hash func test. Tests the basic functionality of the clear Cuckoo hash function
	 * by adding keys from 0 to size -1 of the hash. Tests that entries are where 
	 * they should be. No collisions. Then tests clear to show that the full array is cleared
	 */
	@Test
	@Order(17)
	void BasicHashClearCuckoo_test() {
		hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1);
		System.out.println("Clear Test #3: Cuckoo Hash");
		int size = hash.getTableSize();
		assertEquals(31,size);
		assertEquals(0,hash.size());
		assertTrue(hash.isEmpty());
		for (int i = 0; i < size; i++) {
			assertTrue(hash.add(size*(size+i)));
			assertTrue(hash.add(i));
			assertEquals(size*(size+i),hash.getHashAt(i, 1));
			assertEquals(i, hash.getHashAt(i, 0));
			assertFalse(hash.isEmpty());
		}
		assertEquals(size,hash.getTableSize());
		for (int i = 0; i < hash.getTableSize(); i++) {
			System.out.println("pTable["+i+"]="+hash.getHashAt(i,0)+"\t\tsTable["+i+"]="+hash.getHashAt(i, 1));
		}
		
		hash.clear();
		assertEquals(0, hash.size());
		assertTrue(hash.isEmpty());
		for (int i = 0; i < size; i++) {
			assertEquals(-1,hash.getHashAt(i, 1));
			assertEquals(-1,hash.getHashAt(i, 0));
		}
		assertEquals(size,hash.getTableSize());
	}

	void printHash() {
		System.out.println("Printing HashTable Cuckoo:");
		for (int i =0 ; i < hash.getTableSize(); i++) {
			System.out.println("   ["+i+"] : pTable: "+hash.getHashAt(i, 0) +"   sTable: "+hash.getHashAt(i, 1));
		}

	}

}
