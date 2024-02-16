import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class IteratorTest {
    private List<String> list;

    @Before
    public void setUp() {

        list = new ArrayList<>();
    }

    /* 1 to 3 includes test for hasnext() */

    // Test 1: hasNext C1-F, C5-T,Using hasNext Checking if our Collection is empty from the start
    @Test
    public void testEmptyCollection() {
        Iterator<String> itr = list.iterator();
        assertFalse(itr.hasNext());
    }

    // Test 2:  hasNext(): C1-T, C5-T
    @Test
    public void testMultipleCalls() {
        list.add("element1");
        Iterator<String> itr = list.iterator();
        assertTrue(itr.hasNext());
        assertTrue(itr.hasNext());
    }

    // Test 3:  hasNext(): C1-T, C5-F
   // This test fails!
    @Test(expected = ConcurrentModificationException.class)
    public void testHasNextAfterModification() {
        list.add("element1");
        Iterator<String> itr = list.iterator();
        assertTrue(itr.hasNext()); // should be true, there is an element
        list.add("element2");      // modify the list after iterator creation
        itr.hasNext();             // should throw ConcurrentModificationException
    }

    /* 4 to 7 includes test for next() */

    // Test 4: Calling next() multiple times sequentially
    // C1-T, C2-T, C5-T
    @Test
    public void testSubsequentNextCalls() {
        list.add("element1");
        list.add("element1");
        Iterator<String> itr = list.iterator();// Create an iterator for the list
        assertEquals("element1", itr.next()); // First call to next() should return "item1"
        assertEquals("element2", itr.next()); // Second call to next() should return "item2"
    }


    // Test 5: Next()
    //  C1-F, C2-F, C5-T
    @Test(expected = NoSuchElementException.class)
    public void testNext_OnEmptyList() {
        // No elements are added to the list, ensuring it's empty.
        Iterator<String> itr = list.iterator();
        assertFalse(itr.hasNext()); // Asserting the list has indeed no next element.
        itr.next(); // This call should throw NoSuchElementException.
    }


    // Test 6: Next()
    //  C1-T, C2-F, C5-T

    @Test
    public void testNext_WithNullAndNonNullValues() {
        // Adding a null value and a non-null value to the list.
        list.add(null);
        list.add("element2");
        Iterator<String> itr = list.iterator();

        assertNull(itr.next()); // The first call to next() should return null.
        assertEquals("element2", itr.next()); // The second call to next() should return "element2".
    }

    // Test 7: Next()
    //   C1-T, C2-F, C5-F
    @Test(expected = ConcurrentModificationException.class)
    public void testNextConcurrentModification() {
        list.add("element1");
        list.add("element2");
        Iterator<String> itr = list.iterator();
        itr.next(); // Advance to "element1"
        list.add("element3"); // Modify the list after creating the iterator
        itr.next(); // This should throw ConcurrentModificationException due to the modification.
    }



    /* 8 to 13 includes test for remove() */


    // Test 8: remove()
    //   C1-T, C2-T, C3-T, C4-T, C5-T
    @Test
    public void testRemoveAfterNext() {
        list.add("sad");
        list.add("normal");
        list.add("happy");
        Iterator<String> itr = list.iterator();
        String removedItem = itr.next(); // Advance and get the first item to remove
        itr.remove(); // Now remove it
        assertFalse(list.contains(removedItem)); // Ensure the item is removed
    }

    // Test9 : remove()
    //   C1-F, C2-F, C3-T, C4-T, C5-T
    @Test(expected = NoSuchElementException.class)
    public void testRemoveNoElement() {
        // List is intentionally left empty to have C1-F
        Iterator<String> itr = list.iterator();
        itr.next(); // This should throw NoSuchElementException
    }

    // Test10 :remove()
    // C1-T, C2-F, C3-T, C4-T, C5-T

    @Test
    public void testRemoveNullValue() {
        list.add(null); // Adding a null, making C2-F as we are dealing with a null value
        list.add("delta");
        Iterator<String> itr = list.iterator();
        itr.next(); // Consumes the null
        itr.remove(); // Should remove the null
        assertFalse(list.contains(null)); // Ensure null is removed
    }

    // Test11 :remove()
    // C1-T, C2-T, C3-T, C4-F, C5-T

    @Test(expected = IllegalStateException.class)
    public void testRemove_WithoutProperPrecedingNext() {
        // Initialize the list with elements to satisfy C1-T
        list.add("item1");
        list.add("item2");
        // Creating an iterator and calling next() satisfies C2-T and C3-T
        Iterator<String> itr = list.iterator();
        itr.next();
        itr.remove(); // This is legal and satisfies C3-T
        itr.remove(); // Expected to throw IllegalStateException, since you can't remove twice in a row without calling next() again
    }


    // Test12 :remove()
    //C1-T, C2-T, C3-F, C4-T, C5-T
    @Test(expected = IllegalStateException.class)
    public void testRemove_WithoutNext() {
        list.add("epsilon");
        list.add("zeta");
        Iterator<String> itr = list.iterator();
        itr.remove(); // Attempt to remove without a next() call, should throw IllegalStateException
    }



    // Test13 :remove()
    // C1-T, C2-T, C3-T, C4-T, C5-F

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModification() {
        list.add("rihana");
        list.add("tyla");
        Iterator<String> itr = list.iterator();
        itr.next();
        list.add("fatou");
        itr.remove(); // we are attempting to remove "eta" after list modification, should throw ConcurrentModificationException
    }


}
