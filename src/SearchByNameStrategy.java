import java.util.ArrayList;

public class SearchByNameStrategy extends SearchByButton implements SearchStrategy {
    Player player = null;
    static SearchByNameStrategy instance = null;
    private ArrayList<ItemInterface> stock;

    private SearchByNameStrategy(Player player) {
        super("Name");
        this.player = player;
        stock = new ArrayList<>();
    }

    public static SearchByNameStrategy getInstance(Player player) {
        if (instance == null) {
            instance = new SearchByNameStrategy(player);
        }
        return instance;
    }

    @Override
    public void search(ArrayList<ItemInterface> stock, String term) {
        ArrayList<ItemInterface> result = new ArrayList<>();

        for (ItemInterface item : stock) {
            if (item.getName().toLowerCase().contains(term.toLowerCase())) {
                result.add(item);
            }
        }
        stock.clear();
        stock.addAll(result);
    }
}