package com.github.privacystreams.core;

import com.github.privacystreams.core.providers.mock.MockItem;
import com.github.privacystreams.core.providers.mock.MockObject;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by yuanchun on 19/02/2017.
 * Test cases for Item class
 */
public class ItemTest {
    private MockObject mockObject;
    private Item testItem;

    @Before
    public void setUp() throws Exception {
        this.mockObject = MockObject.getRandomInstance();
        this.testItem = new MockItem(this.mockObject);
    }

    @Test
    public void isEndOfStream() throws Exception {
        Item eosItem = Item.EOS;
        assertTrue(eosItem.isEndOfStream());
    }

    @Test
    public void toMap() throws Exception {
        Map<String, Object> testItemMap = testItem.toMap();
        assertEquals(mockObject.getX(), testItemMap.get(MockItem.X));
        assertEquals(mockObject.getY(), testItemMap.get(MockItem.Y));
        assertEquals(mockObject.getZ(), testItemMap.get(MockItem.Z));
    }

    @Test
    public void toJson() throws Exception {
        JSONObject testItemJson = this.testItem.toJson();
        assertEquals(mockObject.getX(), testItemJson.getInt(MockItem.X));
        assertEquals(mockObject.getY(), testItemJson.getString(MockItem.Y));
    }

    @Test
    public void getValueByField() throws Exception {
        assertEquals(mockObject.getX(), testItem.getValueByField(MockItem.X));
        assertEquals(mockObject.getY(), testItem.getValueByField(MockItem.Y));
    }

    @Test
    public void setFieldValue() throws Exception {
        Item newTestItem = new MockItem(this.mockObject);
        newTestItem.setFieldValue("new_field_key", "new_field_value");
        assertEquals("new_field_value", newTestItem.getValueByField("new_field_key"));
    }

    @Test
    public void containsField() throws Exception {
        assertTrue(this.testItem.containsField(MockItem.X));
        assertFalse(this.testItem.containsField("not_a_field"));
    }

    @Test
    public void includeFields() throws Exception {
        Item newTestItem = new MockItem(this.mockObject);
        newTestItem.includeFields(MockItem.X, MockItem.Y);
        assertTrue(newTestItem.containsField(MockItem.X));
        assertFalse(newTestItem.containsField(MockItem.Z));
    }

    @Test
    public void excludeFields() throws Exception {
        Item newTestItem = new MockItem(this.mockObject);
        newTestItem.excludeFields(MockItem.X, MockItem.Y);
        assertFalse(newTestItem.containsField(MockItem.X));
        assertTrue(newTestItem.containsField(MockItem.Z));
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