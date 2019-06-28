package io.github.privacystreams.multi;

import java.util.List;

import io.github.privacystreams.core.Item;

class ItemWrapper {
    private boolean isList;
    private ItemType.iType type;
    private Item item;
    private List<Item> items;
    ItemWrapper(ItemType.iType type, Item item){
        isList = false;
        this.type = type;
        this.item = item;
    }

    ItemWrapper(ItemType.iType type, List<Item> items){
        isList = true;
        this.type = type;
        this.items = items;
    }

    public boolean isList(){
        return isList;
    }
    public ItemType.iType getType(){
        return type;
    }
    public Item getItem(){
        return item;
    }
    public List<Item> getList(){
        return items;
    }
}
