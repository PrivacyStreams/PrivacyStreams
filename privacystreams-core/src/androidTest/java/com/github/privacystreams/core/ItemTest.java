package com.github.privacystreams.core;

import com.github.privacystreams.core.items.TestItem;
import com.github.privacystreams.core.items.TestObject;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for Item class
 */
public class ItemTest {
    private TestObject testObject;
    private Item testItem;

    @Before
    public void setUp() throws Exception {
        this.testObject = TestObject.getRandomInstance();
        this.testItem = new TestItem(this.testObject);
    }

    @Test
    public void isEndOfStream() throws Exception {
        Item eosItem = Item.EOS;
        assertTrue(eosItem.isEndOfStream());
    }

    @Test
    public void toMap() throws Exception {
        Map<String, Object> testItemMap = testItem.toMap();
        assertEquals(testObject.getX(), testItemMap.get(TestItem.X));
        assertEquals(testObject.getY(), testItemMap.get(TestItem.Y));
        assertEquals(testObject.getZ(), testItemMap.get(TestItem.Z));
    }

    @Test
    public void toJson() throws Exception {
        JSONObject testItemJson = this.testItem.toJson();
        assertEquals(testObject.getX(), testItemJson.getInt(TestItem.X));
        assertEquals(testObject.getY(), testItemJson.getString(TestItem.Y));
    }

    @Test
    public void getValueByField() throws Exception {
        assertEquals(testObject.getX(), testItem.getValueByField(TestItem.X));
        assertEquals(testObject.getY(), testItem.getValueByField(TestItem.Y));
    }

    @Test
    public void setFieldValue() throws Exception {
        Item newTestItem = new TestItem(this.testObject);
        newTestItem.setFieldValue("new_field_key", "new_field_value");
        assertEquals("new_field_value", newTestItem.getValueByField("new_field_key"));
    }

    @Test
    public void containsField() throws Exception {
        assertTrue(this.testItem.containsField(TestItem.X));
        assertFalse(this.testItem.containsField("not_a_field"));
    }

    @Test
    public void includeFields() throws Exception {
        Item newTestItem = new TestItem(this.testObject);
        newTestItem.includeFields(TestItem.X, TestItem.Y);
        assertTrue(newTestItem.containsField(TestItem.X));
        assertFalse(newTestItem.containsField(TestItem.Z));
    }

    @Test
    public void excludeFields() throws Exception {
        Item newTestItem = new TestItem(this.testObject);
        newTestItem.excludeFields(TestItem.X, TestItem.Y);
        assertFalse(newTestItem.containsField(TestItem.X));
        assertTrue(newTestItem.containsField(TestItem.Z));
    }

    @Test
    public void equals() throws Exception {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("x", 1);
        itemMap.put("y", "test");

        Item item1 = new Item(itemMap);
        Item item2 = new Item();
        item2.setFieldValue("x", 1);
        item2.setFieldValue("y", "test");

        assertTrue(item1.equals(item2));
    }

}