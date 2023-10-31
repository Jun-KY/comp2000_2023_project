import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;
// // import src.SearchByName;

public class App {
    private Player player;
    private Storage storage;
    private JFrame frame;
    private PageManager manager;

    public static SearchByButton allButton;
    public static SearchByButton nameButton;
    public static SearchByButton desButton;

    public App(Player p, Storage s) {
        player = p;
        storage = s;
        player.setStorageView(storage.getInventory());

        manager = new PageManager(player, storage);

        allButton = SearchByAllStrategy.getInstance(p);
        allButton.addActionListener((e) -> {
            player.getInventory().setSearch((SearchStrategy) allButton);
            player.getStorageView().setSearch((SearchStrategy) allButton);
        });
        nameButton = SearchByNameStrategy.getInstance(p);
        nameButton.addActionListener((e) -> {
            player.getInventory().setSearch((SearchStrategy) nameButton);
            player.getStorageView().setSearch((SearchStrategy) nameButton);
        });
        desButton = SearchByDesStrategy.getInstance(p);
        desButton.addActionListener((e) -> {
            player.getInventory().setSearch((SearchStrategy) desButton);
            player.getStorageView().setSearch((SearchStrategy) desButton);
        });
        // Setup of sorting
        setupSearching((InventoryPage) manager.findPage("Player Inventory"));
        setupSearching((InventoryPage) manager.findPage("Storage"));

        // Setup of craftng
        setupCrafting((ItemCraftPage) manager.findPage("Item Crafting"), player);
        setupUncrafting((ProductPage) manager.findPage("Product Page"), player);

        // Window creation
        manager.refresh();
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(manager.getJPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Task 1: Defining what each button in the UI will do.
    void setupSearching(InventoryPage page) {
        // // page.addSearchByButton("Search by Name", searchTerm -> {
        // // playerInventory.setSearchStrategy(new SearchByNameStrategy());
        // // return playerInventory.searchItems(searchTerm);
        // // });
        page.addSearchByButton(allButton);
        page.addSearchByButton(nameButton);

        page.addSearchByButton(desButton);
    }

    void setupCrafting(ItemCraftPage page, Player player) {
        page.setCraftAction((def) -> {
            System.out.println("setupCrafting");
            ArrayList<ItemInterface> craftableItems = player.getInventory().searchItems(def.getName());
            Item craftableItem = null;
            if (craftableItems != null) {
                craftableItem = (Item) craftableItems.get(0);
            }
            String[] compStrings = def.componentsString().split(",");
            List<ItemInterface> list = new ArrayList<>();
            for (String s : compStrings) {
                s = s.trim();
                if (s.equals("") == false) {
                    System.out.println("item[" + s + "]");
                    player.getInventory().setSearch(SearchByNameStrategy.getInstance(player));
                    List<ItemInterface> baseItems = player.getInventory().searchItems(s);
                    try {
                        ItemInterface baseItem = baseItems.get(0);
                        list.add(baseItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                        List<ItemInterface> storageItems = player.getStorageView().searchItems(s);
                        String message = null;
                        if (storageItems.size() == 0) {
                            message = "Failed to craft items. Lack of items[" + s + "]";
                        } else {
                            message = "Retrieve items from storage.[" + s + "]";
                        }
                        JOptionPane.showMessageDialog(page, message);
                        return;
                    }
                }
            }
            if (list.size() > 0) {
                for (ItemInterface baseItem : list) {
                    ItemInterface removeItem = player.getInventory().remove(baseItem);
                    craftableItem.add(removeItem); // add for composite pattern.
                }
                def.setWeight(craftableItem.getWeight());
            }
        });
    }

    void setupUncrafting(ProductPage page, Player player) {
        page.setUncraftAction((item) -> {
            System.out.println("setup Uncrafting \nitem : " + item);
            List<ItemInterface> baseItems = item.items;
            if (baseItems.size() > 0) {
                for (int i = baseItems.size() - 1; i >= 0; i--) {
                    ItemInterface removeItem = baseItems.remove(i);
                    player.getInventory().addOne(removeItem);
                }
                item.getDefinition().setWeight(0);
            }
        });
    }
}
