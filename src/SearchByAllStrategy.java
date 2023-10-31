import java.util.ArrayList;

public class SearchByAllStrategy extends SearchByButton implements SearchStrategy {
    Player player = null;
    static SearchByAllStrategy instance = null;
    private ArrayList<ItemInterface> stock;

    private SearchByAllStrategy(Player player) {
        super("All");
        this.player = player;
        stock = new ArrayList<>();
    }

    public static SearchByAllStrategy getInstance(Player player) {
        if (instance == null) {
            instance = new SearchByAllStrategy(player);
        }
        return instance;
    }

    @Override   // reset and call the items again for the next
    public void search(ArrayList<ItemInterface> result, String term) {
        for (int i = 0; i < result.size(); i++) {
            ItemInterface item = result.get(i);
            if (!item.getName().toLowerCase().contains(term.toLowerCase()) 
                && !item.getDescription().toLowerCase().contains(term.toLowerCase())) {
                result.remove(i);
                i--; 
            }
        }
    }
}
