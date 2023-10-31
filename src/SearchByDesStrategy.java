import java.util.ArrayList;

public class SearchByDesStrategy extends SearchByButton implements SearchStrategy {
    Player player = null;
    static SearchByDesStrategy instance = null;
    private ArrayList<ItemInterface> stock;

    private SearchByDesStrategy(Player player) {
        super("Description");
        this.player = player;
        stock = new ArrayList<>();
    }

    public static SearchByDesStrategy getInstance(Player player) {
        if (instance == null) {
            instance = new SearchByDesStrategy(player);
        }
        return instance;
    }

    @Override
    public void search(ArrayList<ItemInterface> stock, String term) {
        ArrayList<ItemInterface> result = new ArrayList<>();

        for (ItemInterface item : stock) {
            if (item.getDescription().toLowerCase().contains(term.toLowerCase())) {
                result.add(item);
            }
        }
        stock.clear();
        stock.addAll(result);
    }
}
