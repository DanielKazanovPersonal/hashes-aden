import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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
 * The Class IntHashBenchmarkBTemplateTest.
 */
@TestMethodOrder(OrderAnnotation.class)
class IntHashBenchmarkDTest {
	
	/**  Constants. */
	
	/** the number of times to collect data for each load limit for each hash */
	private static final int NUM_ITERATIONS = 5;
	
	/** The lf limit. An array for controlling the hash loading */
	private static double[] lfLimit = {0.1, 0.3, 0.5, 0.7, 0.9};
	
	/**  Use to "match" loading of other caches due to cuckoo growth behavior. */
	private static final int[] cuckooLimit = {1001,3003,5005,7007,9009};
	
	/**  Number of load factors to test. */
	private static final int NUM_LOOPS = lfLimit.length;
	
	/**  Number of hash types to test. */
	private static final int NUM_HASHES = 4;
	
	/** number of unique keys to perform hit/miss on for each loop
	 * Note that these are constant for every load factor, and that the
	 * hit array is also used to remove and then re-add keys. Finally,
	 * keep to less than 500 */
	private static final int NUM_HIT_MISS = 100;
	
	/** Number of "buckets" to break the hash into for analysis purposes. The last
	 * bucket may not be the same size as other buckets. */
	private static final int NUM_BUCKETS = 100;
	
	/** The benchmark data . An array for storing all of the data by hash type, load factor and iteration */
	private static double[][][][] bmData = new double[NUM_HASHES][NUM_LOOPS][NUM_ITERATIONS][5];

	/** The analysis data . An array for storing all of the data by hash type, load factor and bucket */
	private static int [][][][] hashAnalysis = new int[NUM_HASHES][NUM_LOOPS][NUM_BUCKETS][2];
	
	/**  The next for value are base indices for bmData and hashAnalysis, based upon the hash type. */
	private static final int LP = 0;
	
	/** The Constant QP. */
	private static final int QP = 1;
	
	/** The Constant LL. */
	private static final int LL = 2;
	
	/** The Constant CUCKOO. */
	private static final int CUCKOO = 3;	

	/**  Array to hold the data from the input file. */
	private static int[] dSet;

	/** The Constant TEST_SIZE. The initial size of the hash table */
	private static final int TEST_SIZE = 10009;

	/**  The data set - the name of the data file - located in data/ directory. */
	private static String dataSet = "dataSetD.csv";

	/**  The time to do 100 contains checks on numbers guaranteed to be in the hash. */
	private double avgContains;

	/**  The time to do 100 contains checks on numbers guaranteed to NOT be in the hash. */
	private double avgContainsMiss;

	/**  The time to request 100 removes - numbers are all initially in the hash. */
	private double avgRemove;

	/** The time to do 100 adds - all numbers initially are NOT in the hash... */
	private double avgAdd;

	/** The hash. */
	private MyIntHash hash;

	/**
	 * Sets the up before class. Checks to make sure that the data file exists, has data and can be read
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File inputFileA = new File("data/"+dataSet);
		if (!inputFileA.exists() || inputFileA.length() == 0 || !inputFileA.canRead()) {
			System.out.println("Problem opening file: data/"+dataSet);
			fail();
		}	
		processInputFile(inputFileA);

	}

	/**
	 * Write bench mark data to a unique file in the "data/" directory.
	 * Data organization:
	 * 	 For each hashType (row):
	 *      For each load factor (row):
	 *          For each iteration (row):
	 *             report Avg contains, Avg miss, Avg remove, Avg Add and table size (column)
	 *
	 * @param outFile the filename to write the data to.
	 */
	static void writeBenchMarkData(String outFile) {
		File outputFileA = new File("data/"+outFile);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileA));
			System.out.println("Writing Benchmark data to file: "+outputFileA.getName());
			String hashImp = "";

			bw.write("Hash Implementation,LoadFactor,Iteration,Avg Contains (hit), Avg Contains (miss), Avg Remove, Avg Add\n");
			for (int ht = 0; ht < NUM_HASHES;ht ++) {
				if (ht == LP) hashImp = "Linear Probing";
				if (ht == QP) hashImp = "Quadratic Probing";
				if (ht == LL) hashImp = "LinkedList";
				if (ht == CUCKOO) hashImp = "Cuckoo";
				for (int lp = 0; lp < NUM_LOOPS; lp++) {
					for (int iter = 0; iter < NUM_ITERATIONS; iter++) {
						String outStr = hashImp+","+lfLimit[lp]+","+iter+","+bmData[ht][lp][iter][0]+","+bmData[ht][lp][iter][1]+","+bmData[ht][lp][iter][2]+","+bmData[ht][lp][iter][3]+","+bmData[ht][lp][iter][4]+"\n";
						bw.write(outStr);
					}
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Write hash analysis data to a file in the "data/" directory
	 * Data organization:
	 * For each hashType (row):
	 *    For each chunk of addresses (row):
	 *       For each load factor (column):
	 *       	report #valid, #empty
	 *
	 * @param outFile the filename of file that will hold the data
	 */
		static void writeHashAnalysisData(String outFile) {
		File outputFileA = new File("data/"+outFile);
		String outStr = "";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileA));
			System.out.println("Writing Benchmark data to file: "+outputFileA.getName());
			String hashImp = "";

			bw.write("Hash Implementation,chunk");
			for (int i = 0; i < NUM_LOOPS; i++) {
				bw.write(",lf "+lfLimit[i]+" valid,lf "+lfLimit[i]+" empty");	
			}
			bw.write("\n");
			for (int i = 0; i < NUM_HASHES ;i ++) {
				if (i == LP) hashImp = "Linear Probing";
				if (i == QP) hashImp = "Quadratic Probing";
				if (i == LL) hashImp = "LinkedList";
				if (i == CUCKOO) hashImp = "Cuckoo";
				for (int chunk = 0; chunk < NUM_BUCKETS; chunk++) {
					outStr = hashImp+","+chunk;
					for (int lp = 0; lp < NUM_LOOPS; lp++) {
						outStr += ","+hashAnalysis[i][lp][chunk][0]+","+hashAnalysis[i][lp][chunk][1];
					}
					bw.write(outStr+"\n");
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Tear down after class. Writes the benchmark and hash analysis data
	 * to a unique file.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		String outFile = dataSet.replaceAll(".csv", "_bm.csv");
		writeBenchMarkData(outFile);
		outFile = dataSet.replaceAll(".csv", "_ha.csv");
		writeHashAnalysisData(outFile);

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
	 * Benchmark Linear Probing Hash test. 
	 * Measures the performance of the linear probing hash at 5 different load conditions
	 * Repeats the measurements NUM_ITERATION times (iter loop) to check variability
	 * Algorithm:
	 * 
	 *     for each load limit:
	 *         sequentially add keys from the dSet to the hash until it reaches the load limit
	 *             keeping track of the index where you reach the load limit
	 *         generate a set of NUM_HIT_MISS random keys that are guaranteed to hit the hash
	 *            (select from the first index of the dSet to the load limit index)
	 *         generate a set of NUM_HIT_MISS random keys that are guaranteed to miss the hash
	 *            (select from the load limit index of the dSet to the last index)
	 *         measure: 
	 *             NUM_HIT_MISS contains using hit[]
	 *             NUM_HIT_MISS contains using miss[]
	 *             NUM_HIT_MISS removes using hit[]
	 *             NUM_HIT_MISS adds using hit[]
	 *         report results.
	 *        
	 */
	@Test
	@Order(1)
	void benchmark_LP_test() {
		int[][] hit = new int[NUM_LOOPS][NUM_HIT_MISS];
		int[][] miss = new int[NUM_LOOPS][NUM_HIT_MISS];

		for (int iter = 0; iter <NUM_ITERATIONS; iter++) {    // loop through the testing 5 times
			int dataIndex = 0;
			hash = new MyIntHash(MyIntHash.MODE.Linear,1.1,TEST_SIZE);
			System.out.println("\n\nBenchmark "+dataSet.replaceAll(".csv","")+" Linear Hash Implementation - Iteration "+iter);
			for (int loop = 0; loop < lfLimit.length; loop++) {
				// TODO: add elements to the hash until it is just greater than the load factor limit
				double loadFactorLimit = hash.getCurrHashLoad();
				while (loadFactorLimit < lfLimit[loop]) {
					hash.add(dSet[dataIndex++]);
					loadFactorLimit = hash.getCurrHashLoad();
				}
				// this will generate a hundred random keys that will exist or miss in the hash
				if (iter == 0 ) {
					hit[loop] = genNRandomInts(Arrays.copyOfRange(dSet,0, dataIndex));
					miss[loop] = genNRandomInts(Arrays.copyOfRange(dSet,dataIndex,dSet.length - 1));
				}

				// measure the time to do NUM_HIT_MISS contains checks on numbers guaranteed to be in the hash
				avgContains = measureContains(hit[loop]);
				// measure the time to do NUM_HIT_MISS contains checks on numbers guaranteed to NOT be in the hash
				avgContainsMiss = measureContains(miss[loop]);
				// measure the time to request NUM_HIT_MISS removes - number are all initially in the hash
				avgRemove = measureRemove(hit[loop]);
				// measure the time to do NUM_HIT_MISS inserts - all numbers initially are NOT in the hash...
				avgAdd = measureAdd(hit[loop]);

				reportData(LP,loop,iter);
				if (iter == 0) {
					analyzeHash(LP,loop);
				}

			}
		}
		assertTrue(true);
	}

	/**
	 * Benchmark Quadratic Probing test.
	 * Measures the performance of the quadratic probing hash at 5 different load conditions
	 * Repeats the measurements NUM_ITERATION times (iter loop) to check variability
	 * Algorithm:
	 * 
	 *     for each load limit:
	 *         sequentially add keys from the dSet to the hash until it reaches the load limit
	 *             keeping track of the index where you reach the load limit
	 *         generate a set of NUM_HIT_MISS random keys that are guaranteed to hit the hash
	 *            (select from the first index of the dSet to the load limit index)
	 *         generate a set of NUM_HIT_MISS random keys that are guaranteed to miss the hash
	 *            (select from the load limit index of the dSet to the last index)
	 *         measure: 
	 *             NUM_HIT_MISS contains using hit[]
	 *             NUM_HIT_MISS contains using miss[]
	 *             NUM_HIT_MISS removes using hit[]
	 *             NUM_HIT_MISS adds using hit[]
	 *         report results.
	 *                 
	 */
	@Test
	@Order(2)
	void benchmark_QP_test() {
		int[][] hit = new int[NUM_LOOPS][NUM_HIT_MISS];
		int[][] miss = new int[NUM_LOOPS][NUM_HIT_MISS];
		for (int iter = 0; iter <NUM_ITERATIONS; iter++) {
			hash = new MyIntHash(MyIntHash.MODE.Quadratic,1.1,TEST_SIZE);
			int dataIndex = 0;
			System.out.println("\n\nBenchmark "+dataSet.replaceAll(".csv","")+" Quadratic Hash Implementation - Iteration "+iter);
			// TODO: Write this method - but use QP for reporting the data
			for (int loop = 0; loop < lfLimit.length; loop++) {
				// TODO: add elements to the hash until it is just greater than the load factor limit
				double loadFactorLimit = hash.getCurrHashLoad();
				while (loadFactorLimit < lfLimit[loop]) {
					hash.add(dSet[dataIndex++]);
					loadFactorLimit = hash.getCurrHashLoad();
				}
				// this will generate a hundred random keys that will exist or miss in the hash
				if (iter == 0 ) {
					hit[loop] = genNRandomInts(Arrays.copyOfRange(dSet,0, dataIndex));
					miss[loop] = genNRandomInts(Arrays.copyOfRange(dSet,dataIndex,dSet.length - 1));
				}

				// measure the time to do NUM_HIT_MISS contains checks on numbers guaranteed to be in the hash
				avgContains = measureContains(hit[loop]);
				// measure the time to do NUM_HIT_MISS contains checks on numbers guaranteed to NOT be in the hash
				avgContainsMiss = measureContains(miss[loop]);
				// measure the time to request NUM_HIT_MISS removes - number are all initially in the hash
				avgRemove = measureRemove(hit[loop]);
				// measure the time to do NUM_HIT_MISS inserts - all numbers initially are NOT in the hash...
				avgAdd = measureAdd(hit[loop]);

				reportData(QP,loop,iter);
				if (iter == 0) {
					analyzeHash(QP,loop);
				}

			}
		}
		assertTrue(true);
	}

	/**
	 * Benchmark LinkedList Hash test.
	 * Measures the performance of the LinkedList hash at 5 different load conditions
	 * Repeats the measurements NUM_ITERATION times (iter loop) to check variability
	 * Algorithm:
	 * 
	 *     for each load limit:
	 *         sequentially add keys from the dSet to the hash until it reaches the load limit
	 *             keeping track of the index where you reach the load limit
	 *         generate a set of NUM_HIT_MISS random keys that are guaranteed to hit the hash
	 *            (select from the first index of the dSet to the load limit index)
	 *         generate a set of NUM_HIT_MISS random keys that are guaranteed to miss the hash
	 *            (select from the load limit index of the dSet to the last index)
	 *         measure: 
	 *             NUM_HIT_MISS contains using hit[]
	 *             NUM_HIT_MISS contains using miss[]
	 *             NUM_HIT_MISS removes using hit[]
	 *             NUM_HIT_MISS adds using hit[]
	 *         report results.
	 *        
	 */
	@Test
	@Order(3)	
	void benchmark_LL_test() {
		int[][] hit = new int[NUM_LOOPS][NUM_HIT_MISS];
		int[][] miss = new int[NUM_LOOPS][NUM_HIT_MISS];
		for (int iter = 0; iter <NUM_ITERATIONS; iter++) {
			hash = new MyIntHash(MyIntHash.MODE.LinkedList,1.1,TEST_SIZE);
			int dataIndex = 0;
			System.out.println("\n\nBenchmark "+dataSet.replaceAll(".csv","")+" LinkedList Hash Implementation - Iteration "+iter);
			//TODO: Write this method - but use LL for reporting the data
			for (int loop = 0; loop < lfLimit.length; loop++) {
				// TODO: add elements to the hash until it is just greater than the load factor limit
				double loadFactorLimit = hash.getCurrHashLoad();
				while (loadFactorLimit < lfLimit[loop]) {
					hash.add(dSet[dataIndex++]);
					loadFactorLimit = hash.getCurrHashLoad();
				}
				// this will generate a hundred random keys that will exist or miss in the hash
				if (iter == 0 ) {
					hit[loop] = genNRandomInts(Arrays.copyOfRange(dSet,0, dataIndex));
					miss[loop] = genNRandomInts(Arrays.copyOfRange(dSet,dataIndex,dSet.length - 1));
				}

				// measure the time to do NUM_HIT_MISS contains checks on numbers guaranteed to be in the hash
				avgContains = measureContains(hit[loop]);
				// measure the time to do NUM_HIT_MISS contains checks on numbers guaranteed to NOT be in the hash
				avgContainsMiss = measureContains(miss[loop]);
				// measure the time to request NUM_HIT_MISS removes - number are all initially in the hash
				avgRemove = measureRemove(hit[loop]);
				// measure the time to do NUM_HIT_MISS inserts - all numbers initially are NOT in the hash...
				avgAdd = measureAdd(hit[loop]);

				reportData(LL,loop,iter);
				if (iter == 0) {
					analyzeHash(LL,loop);
				}

			}
		}
		assertTrue(true);
	}

	/**
	 * Benchmark Cuckoo Hash test.
	 * Measures the performance of the Cuckoo hash at 5 different load conditions
	 * Repeats the measurements NUM_ITERATION times (iter loop) to check variability
	 * Algorithm:
	 * 
	 *     for each value in cuckooLoop:
	 *         sequentially add keys from the dSet to the hash until it reaches the load limit
	 *             keeping track of the index where you reach the load limit
	 *         generate a set of NUM_HIT_MISS random keys that are guaranteed to hit the hash
	 *            (select from the first index of the dSet to the load limit index)
	 *         generate a set of NUM_HIT_MISS random keys that are guaranteed to miss the hash
	 *            (select from the load limit index of the dSet to the last index)
	 *         measure: 
	 *             NUM_HIT_MISS contains using hit[]
	 *             NUM_HIT_MISS contains using miss[]
	 *             NUM_HIT_MISS removes using hit[]
	 *             NUM_HIT_MISS adds using hit[]
	 *         report results.
	 *        
	 */
	@Test
	@Order(4)
	void benchmark_Cuckoo_test() {
		int[][] hit = new int[NUM_LOOPS][NUM_HIT_MISS];
		int[][] miss = new int[NUM_LOOPS][NUM_HIT_MISS];
		for (int iter = 0; iter <NUM_ITERATIONS; iter++) {
			hash = new MyIntHash(MyIntHash.MODE.Cuckoo,1.1,TEST_SIZE);
			int dataIndex = 0;
			System.out.println("\n\nBenchmark "+dataSet.replaceAll(".csv","")+" Cuckoo Hash Implementation - Iteration "+iter);
			//TODO: Write this method - but use CUCKOO for reporting the data and cuckooLoop for the hash limit
			for (int loop = 0; loop < lfLimit.length; loop++) {
				// TODO: add elements to the hash until it is just greater than the load factor limit
				while (hash.size() < cuckooLimit[loop]) {
					hash.add(dSet[dataIndex++]);
				}
				// this will generate a hundred random keys that will exist or miss in the hash
				if (iter == 0 ) {
					hit[loop] = genNRandomInts(Arrays.copyOfRange(dSet,0, dataIndex));
					miss[loop] = genNRandomInts(Arrays.copyOfRange(dSet,dataIndex,dSet.length - 1));
				}

				// measure the time to do NUM_HIT_MISS contains checks on numbers guaranteed to be in the hash
				avgContains = measureContains(hit[loop]);
				// measure the time to do NUM_HIT_MISS contains checks on numbers guaranteed to NOT be in the hash
				avgContainsMiss = measureContains(miss[loop]);
				// measure the time to request NUM_HIT_MISS removes - number are all initially in the hash
				avgRemove = measureRemove(hit[loop]);
				// measure the time to do NUM_HIT_MISS inserts - all numbers initially are NOT in the hash...
				avgAdd = measureAdd(hit[loop]);

				reportData(CUCKOO,loop,iter);
				if (iter == 0) {
					analyzeHash(CUCKOO,loop);
				}

			}
		}
		assertTrue(true);
	}

	/**
	 * Measure average contains time across 100 accesses.
	 *
	 * @param a the array of random keys to use for the test
	 * @return the time measurement in appropriate units for the average access time
	 */
	private double measureContains(int a[]) {
		long start;
		long stop;

		start = System.nanoTime();
		//TODO: write the code to perform contains() on each key in a[]
		for (int i = 0; i < a.length; i++) {
			hash.contains(i);
		}
		stop = System.nanoTime();
		return (double) ((stop - start)/1E5);
	}

	/**
	 * Measure average remove time across 100 acesses.
	 *
	 * @param a the array of random keys to use for the test
	 * @return the time measurement in appropriate units for the average access time
	 */
	private double measureRemove(int a[]) {
		long start;
		long stop;

		start = System.nanoTime();
		//TODO: write the code to perform remove() on each key in a[]
		for (int i = 0; i < a.length; i++) {
			hash.remove(i);
		}
		stop = System.nanoTime();
		return (double) ((stop - start)/1E5);
	}

	/**
	 * Measure average add time across 100 accesses.
	 *
	 * @param a the array of random keys to use for the test
	 * @return the time measurement in appropriate units for the average access time
	 */
	private double measureAdd(int a[]) {
		long start;
		long stop;

		start = System.nanoTime();
		//TODO: write the code to perform add() on each key in a[]
		for (int i = 0; i < a.length; i++) {
			hash.add(i);
		}
		stop = System.nanoTime();
		return (double) ((stop - start)/1E5);
	}

	/**
	 * Analyze hash - this function calls the individual routines to 
	 * look at the layout of the hash based upon load factor. Refer 
	 * to analyzeLPHash to get an idea of what you need to do to
	 * write the QP, LL and Cuckoo versions.
	 *
	 * @param mode - which hashType
	 * @param loop - indicates which load factor resulted in this hash image.
	 */
	private void analyzeHash(int mode, int loop) {
		switch (mode) {
		case LP : analyzeLPHash(loop); break;
		case QP : analyzeQPHash(loop); break;
		case LL : analyzeLLHash(loop); break;
		case CUCKOO : analyzeCuckooHash(loop); break;
		}
	}
	
	/**
	 * Analyze LP hash.
	 * 1) breaks the hash table into NUM_BUCKETS approximately equal-sized
	 *    chunks of indexes (the last chunk will be different).
	 *    Example: for 100 buckets and a tableSize of 10009,
	 *    	each chunk will have a size of 101, 
	 *    	chunk 0 will cover hashTable indexes from 0-100,
	 *    	chunk 1 will cover hashTable indexes from 101-201,
	 *      chunk 2 will cover hashTable indexes from 202-302
	 *      chunk 98 will cover hashTable indexes from 9898 - 9998
	 *      chunk 99 will cover hashTable indexes from 9999 - 10008
	 *  
	 * 2) For each chunk:
	 *    - clear valid/empty values     
	 *    - walk through every index in the chunk; if the data is valid,
	 *      increment the valid, else increment empty (there will be no removed
	 *      entries).
	 *    - record the valid/empty data for this hashType, load factor, and chunk.
	 *    
	 * Take note of the nested while and for loops - and that the hashTable index
	 * (htIndex) is incremented and not reset by the for loop.
	 *
	 * @param loop indicates which load factor was used for this hash
	 */
	private void analyzeLPHash(int loop) {
		int hashType=0;
		int tableSize = hash.getTableSize();
		int chunk = tableSize/NUM_BUCKETS + 1;  // ~ 1% of the hash
		int htIndex = 0;
		int dataIndex = 0;
		while (htIndex < tableSize ) {
			int valid = 0;
			int empty = 0;
			for (int i = 0; (i < chunk) && (htIndex< tableSize); i++ ) {
				int data = hash.getHashAt(htIndex, 0);
				if (data >= 0 ) 
					valid++;
				else
					empty++;
				htIndex++;
			}
			hashAnalysis[hashType][loop][dataIndex][0] = valid;
			hashAnalysis[hashType][loop][dataIndex][1] = empty;
			dataIndex++;
		}
	}

	/**
	 * Analyze QP hash.
	 * 
	 * @param loop indicates which load factor was used for this hash
	 */
	private void analyzeQPHash(int loop) {
		int hashType=QP;
		int tableSize = hash.getTableSize();
		int chunk = tableSize/NUM_BUCKETS + 1;  // ~ 1% of the hash
		int htIndex = 0;
		int dataIndex = 0;
		while (htIndex < tableSize ) {
			int valid = 0;
			int empty = 0;
			for (int i = 0; (i < chunk) && (htIndex< tableSize); i++ ) {
				int data = hash.getHashAt(htIndex, 0);
				if (data >= 0 ) 
					valid++;
				else
					empty++;
				htIndex++;
			}
			hashAnalysis[hashType][loop][dataIndex][0] = valid;
			hashAnalysis[hashType][loop][dataIndex][1] = empty;
			dataIndex++;
		}
	}
	
	/**
	 * Analyze LL hash.
	 * 
	 * @param loop indicates which load factor was used for this hash
	 */
	private void analyzeLLHash(int loop) {
		int hashType=LL;
		int tableSize = hash.getTableSize();
		int chunk = tableSize/NUM_BUCKETS + 1;  // ~ 1% of the hash
		int htIndex = 0;
		int dataIndex = 0;
		while (htIndex < tableSize ) {
			int valid = 0;
			int empty = 0;
			for (int i = 0; (i < chunk) && (htIndex< tableSize); i++ ) {
				Integer data = hash.getHashAt(htIndex, 0);
				if (data != null && data.intValue() >= 0) {
					valid++;
				}
				else
					empty++;
				htIndex++;
			}
			hashAnalysis[hashType][loop][dataIndex][0] = valid;
			hashAnalysis[hashType][loop][dataIndex][1] = empty;
			dataIndex++;
		}
	}
	
	/**
	 * Analyze Cuckoo hash.
	 * 
	 * @param loop indicates which load factor was used for this hash
	 */
	private void analyzeCuckooHash(int loop) {
		int hashType=CUCKOO;
		int tableSize = hash.getTableSize();
		int chunk = tableSize/NUM_BUCKETS + 1;  // ~ 1% of the hash
		int htIndex = 0;
		int dataIndex = 0;
		while (htIndex < tableSize ) {
			int valid = 0;
			int empty = 0;
			for (int i = 0; (i < chunk) && (htIndex< tableSize); i++ ) {
				int data = hash.getHashAt(htIndex, 0);
				if (data >= 0 ) 
					valid++;
				else
					empty++;
				data = hash.getHashAt(htIndex, 1);
				if (data >= 0 ) 
					valid++;
				else
					empty++;
				htIndex++;
			}
			hashAnalysis[hashType][loop][dataIndex][0] = valid;
			hashAnalysis[hashType][loop][dataIndex][1] = empty;
			dataIndex++;
		}
	}
	
	/**
	 * Gen NUM_HIT_MISS random ints.
	 *
	 * @param ar the array of keys that already exist in the hash
	 * @return an array of the 1st 100 randomly ordered keys
	 */
	private int[] genNRandomInts(int ar[]) {
		shuffleArray(ar);
		return (Arrays.copyOfRange(ar,0,NUM_HIT_MISS));
	}

	/**
	 * Process input file as a comma separated list, and store each key in dSet.
	 *
	 * @param in The input File object
	 * @throws Exception the exception
	 */
	private static void processInputFile(File in) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = br.readLine();
		String[] data = line.split(",");
		dSet = new int[data.length];
		for (int i = 0 ; i < data.length ; i++) 
			dSet[i] = Integer.parseInt(data[i]);
		br.close();
	}

	/**
	 * Shuffle array. Randomizes an array of integer primatives
	 * 
	 * implementation from https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
	 *
	 * @param ar the integer array of values. Data is reordered in the array itself
	 */
	// 
	private void shuffleArray(int[] ar) {
		Random rnd = new Random();
		for (int i = ar.length-1; i >0 ; i --) {
			int index = rnd.nextInt(i+1);
			// swap i, index
			int tmp = ar[index];
			ar[index] = ar[i];
			ar[i] = tmp;
		}
	}
	
	/**
	 * Report data.
	 *
	 * @param bmIndex the bm index
	 * @param loop the loop
	 * @param iter the iter
	 */
	private void reportData(int bmIndex,int loop, int iter) {
		System.out.printf("%3.1f,%10.6f,%10.6f,%10.6f,%10.6f, %d\n",lfLimit[loop],avgContains,avgContainsMiss,avgRemove,avgAdd,hash.getTableSize());
		bmData[bmIndex][loop][iter][0]=avgContains;
		bmData[bmIndex][loop][iter][1]=avgContainsMiss;
		bmData[bmIndex][loop][iter][2]=avgRemove;
		bmData[bmIndex][loop][iter][3]=avgAdd;
		bmData[bmIndex][loop][iter][4]=hash.getTableSize();
	}
}
