package com.myjavasolutions.java7.tutorial;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DemoTest {

	Demo demo;
	
	@Before
	public void setUp() {
		demo = new Demo();
	}
	
	@Test
	public void test_switch_with_string_type() {
		
		User admin = new User("admin");
		String accessLevel = demo.getUserAccessLevel(admin);
		assertEquals("admin", accessLevel);
		
		User customer = new User("customer");
		accessLevel = demo.getUserAccessLevel(customer);
		assertEquals("customer", accessLevel);
		
		User user = new User("user");
		accessLevel = demo.getUserAccessLevel(user);
		assertEquals("user", accessLevel);
		
		User userNoAccess = new User("");
		accessLevel = demo.getUserAccessLevel(userNoAccess);
		assertEquals("no access", accessLevel);
	}
	
	@Test
	public void test_diamond_operator() {
		
		//no need to repeat the type in ArrayList
		List<User> users = new ArrayList<>();
		users.add(new User("admin"));
		
		assertTrue(users.size() == 1);
	}
	
	@Test
	public void test_numeric_literals_with_underscores() {
		
		//Java 7 allows you to put underscores between numeric values
		//the following number is easier to read than the one in assert statement
		int twoMillion = 2_000_000;
		
		assertEquals(2000000, twoMillion);
	}
	
	@Test
	public void test_automatic_resource_management() {

		//declaring multiple resources in the try statement
		//JVM will close the resources automatically when after the try block
		//they are closed in the opposite definition order. In our example DataOutputStream closed first then
		//FileOutputStream closed.
		try (FileOutputStream fos = new FileOutputStream("somefile.txt");
				DataOutputStream dos = new DataOutputStream(fos)) {

			dos.writeUTF("Java 7 Rocks!");
			
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test(expected=FileNotFoundException.class)
	public void test_try_block_without_catch_and_finally() throws FileNotFoundException, IOException {

		//declaring multiple resources in the try statement without a catch or finally block
		//generate exception by not passing the file name
		try (FileOutputStream fos = new FileOutputStream("");
				DataOutputStream dos = new DataOutputStream(fos)) {
			dos.writeUTF("Java 7 Rocks!");
		} 
	}
	
	@Test(expected=RuntimeException.class)
	public void test_suppressed_exception() throws Exception {

		//demonstrating suppressed exceptions
		//in the following code two exceptions are generated (one from calling myResource.doSomething() and one on the implicitly called close method)
		//exceptions thrown from the try-with-resources statement are suppressed, and the exception thrown by the block is the one that is thrown by the method
		//in this case we expect RuntimeException
		try (MyResource myResource = new MyResource();) {
			myResource.doSomething();
		} 
	}
	
	@Test
	public void test_obtain_suppressed_exception_from_exception() {

		//in the following code two exceptions are generated (one from calling myResource.doSomething() and one on the implicitly called close method)
		//exceptions thrown from the try-with-resources statement are suppressed
		//the following example shows how you can obtain the suppressed  exceptions from the exception
		try (MyResource myResource = new MyResource();) {
			myResource.doSomething();
		} catch (Exception e) {
		
			assertEquals("java.lang.RuntimeException: Some error occured while processing your request", e.toString());
			Throwable[] suppressedExceptions = e.getSuppressed();
			final int numberOfSupressedExceptions = suppressedExceptions.length;
			
			if (numberOfSupressedExceptions > 0) {
				assertEquals(1, numberOfSupressedExceptions);
				for (final Throwable exception : suppressedExceptions) {
					assertEquals("java.lang.UnsupportedOperationException: Not supported", exception.toString());
				}
			}
		} 
	}
	
	@Test
	public void test_new_exceptional_handling() {
		
		//In the following example, multiple exceptions are caught in one catch block by using a | operator. 
		//This allows a cleaner code.
		try {
			demo.generateParsingException();
			demo.generateIOException();
			
		} catch (IOException | DatatypeConfigurationException e ) {
			assertNotNull(e);
		}
	}
	
	@Test
	public void test_binary_literals() {
		
		//much easier to use binary numbers	while manipulating bits.
		int five = 0b101;
		int four = 0b100;
		int result = five + four;
		
		assertEquals(9, result);
	}
	
	@Test
	public void test_new_IO() throws IOException {
		
		//obtain the current working directory
		String workingDir = System.getProperty("user.dir");
		   
		//A Path is a reference to a file or directory
		Path projectRoot = Paths.get(workingDir);
		
		//create a temporary directory under the project root folder
		Path tempDirectory = Files.createTempDirectory(projectRoot, "temp_");
		
		System.out.println("Directory Name:" + tempDirectory.getFileName());
		
		//path to the temporary file that we just created
		Path tempFile = Files.createTempFile(tempDirectory, "begin_", "_end");
		
		//add some content to the file
		Files.write(tempFile, "Writing some text to the file\n".getBytes());
		
		System.out.println("Content: " + Files.readAllLines(tempFile, Charset.defaultCharset()));
		
		//delete the file first then the directory
		Files.deleteIfExists(tempFile);
		Files.deleteIfExists(tempDirectory);
	}

	@Test
	public void test_new_IO_for_regular_files() throws IOException {

		// obtain the current working directory
		String workingDir = System.getProperty("user.dir");

		// A Path is a reference to a file or directory
		Path projectRoot = Paths.get(workingDir);

		// create a temporary directory under the project root folder
		Path tempDirectory = Files.createTempDirectory(projectRoot, "temp_");

		// Creating a regular file
		Path regularFilePath = Files.createFile(Paths.get(tempDirectory.toString(), "somefile.txt"));

		Files.write(regularFilePath, "This is a sample text".getBytes());
		
		//read all the lines from the file
		List<String> lines = Files.readAllLines(regularFilePath, Charset.defaultCharset());
		
		 for (String line : lines) {
             System.out.println(line);
         }
		 
		// delete the file first then the directory
		Files.deleteIfExists(regularFilePath);
		Files.deleteIfExists(tempDirectory);
	}
	
	/*
	 * The following test shows how you can create a Java program that can monitor a particular
	 * directory. When you run the following test, it goes to infinite loop to listen the directory.
	 * After running the test, go to the directory and add new files and see the console out.
	 * To stop the infinite loop, just delete the temporary directory that is created during this
	 * test.
	 */
	@Ignore("This test listens a directory with infinite loop. To go out of infinite loop, just delete the temporary directory that is created during this  test")
	@Test
	public void test_new_IO_file_change_notification() throws IOException, InterruptedException {
		
		//the following example shows how to create a Java program that will monitor a particular directory.
		WatchService  watchService = FileSystems.getDefault().newWatchService();
		
		// let's create a directory
		String workingDir = System.getProperty("user.dir");
		Path projectRoot = Paths.get(workingDir);
		// create a temporary directory under the project root folder
		Path tempDirectory = Files.createTempDirectory(projectRoot, "temp_");
		
		//register the directory with the WatchService 
		tempDirectory.register(watchService, OVERFLOW, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		
		//infinite loop to start taking the events
		//while looping, go to the directory and add new files and see the console out
		//To stop the infinite loop, just delete the temporary directory that is created during this  test
		while(true)
		{
		    WatchKey key = watchService.take();
		    
		    for (WatchEvent<?> event : key.pollEvents()) {

	            Kind<?> kind = event.kind();

	            System.out.println("Event: " + event.context().toString() + " Event Type: " + kind);

		    }
			
			 boolean reset = key.reset();
			 
			 if(!reset) {
				 break;
			 }
		}
	}
	
	@Test
	public void test_fork_and_join(){
		
		int numberOfProcessors = Runtime.getRuntime().availableProcessors();

		// Create the ForkJoinPool that will run the task
		final ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfProcessors);

		// Create a task that represents all of the work to be done
		final MyExpensiveTask myExpensiveTask = new MyExpensiveTask();

		// Run the task
		forkJoinPool.invoke(myExpensiveTask);
	}
}