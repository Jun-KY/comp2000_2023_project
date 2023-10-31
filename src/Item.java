import java.util.ArrayList;
import java.util.List;

public class Item implements ItemInterface {
    private ItemDefinition definition;
    List<ItemInterface> items = new ArrayList<>();

    /**
     * Creates an Item instance with a set definition.
     * The composition list is (created but) left empty. For composite items, the
     * sub-components
     * should be retrieved/removed from some item source, and added with
     * Item::Add(ItemInterface).
     * 
     * @param def
     */
    public Item(ItemDefinition def) {
        definition = def;
    }

    @Override
    public double getWeight() {
        double weight = definition.getWeight().orElse(0.0);
        // If the item is made up of other items, we should find the sum of weights
        if (items.size() == 0) {
            weight = definition.getWeight().orElse(0.0);
        } else {
            for (ItemInterface item : items) {
                weight += item.getWeight();
            }
        }
        return weight;
    }

    public void add(ItemInterface item) {
        this.items.add(item);
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public String getDescription() {
        return definition.getDescription();
    }

    @Override
    public ItemDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getCompositionDescription() {
        // For craftable items, this method should return a description
        // describing/listing the
        // other items which make up this item.
        // When a non-empty String is returned, the uncraft button will appear in the
        // UI.
        return definition.getDescription();
    }

    @Override
    public boolean equals(ItemInterface other) {
        return isOf(other.getDefinition());
    }

    @Override
    public boolean isOf(ItemDefinition def) {
        return getName().equals(def.getName());
    }

    @Override
    public String toString() {
        String output = String.format("Item: %s\nDescription: %s\nWeight: %.2f",
                getName(), getDescription(), getWeight());
        output += "\nHashCode: " + Integer.toHexString(this.hashCode());
        return output;
    }

}