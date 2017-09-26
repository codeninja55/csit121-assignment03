package styles;
import javax.swing.*;

@SuppressWarnings("All")
public abstract class IconFactory {
    public static ImageIcon homeIcon() { return new ImageIcon("com.cn55.marvel/src/img/home_white_48.png"); }
    public static ImageIcon cardIcon() { return new ImageIcon("com.cn55.marvel/src/img/card_membership_white_48.png"); }
    public static ImageIcon purchaseIcon() { return new ImageIcon("com.cn55.marvel/src/img/shopping_cart_white_48.png"); }
    public static ImageIcon categoryIcon() { return new ImageIcon("com.cn55.marvel/src/img/view_list_white_48.png"); }
    public static ImageIcon summaryViewPaneIcon() { return new ImageIcon("com.cn55.marvel/src/img/assessment_white_48.png"); }
    public static ImageIcon refreshIcon() { return new ImageIcon("com.cn55.marvel/src/img/refresh_white_48.png"); }
    public static ImageIcon createIcon() { return new ImageIcon("com.cn55.marvel/src/img/add_circle_white_36.png"); }
    public static ImageIcon deleteIcon() { return new ImageIcon("com.cn55.marvel/src/img/remove_circle_white_36.png"); }
    public static ImageIcon deleteActionIcon() { return new ImageIcon("com.cn55.marvel/src/img/delete_white_36.png"); }
    public static ImageIcon deleteIconDisabled() { return new ImageIcon("com.cn55.marvel/src/img/remove_circle_grey_36.png"); }
    public static ImageIcon searchIcon() { return new ImageIcon("com.cn55.marvel/src/img/search_white_36.png"); }
    public static ImageIcon viewIcon() { return new ImageIcon("com.cn55.marvel/src/img/visibility_white_36.png"); }
    public static ImageIcon summaryIcon() { return new ImageIcon("com.cn55.marvel/src/img/assessment_white_36.png"); }
    public static ImageIcon cancelIcon() { return new ImageIcon("com.cn55.marvel/src/img/cancel_white_36.png");}
    public static ImageIcon cancelIconDisabled() { return new ImageIcon("com.cn55.marvel/src/img/cancel_grey_36.png");}
    public static ImageIcon clearIcon() { return new ImageIcon("com.cn55.marvel/src/img/clear_all_white_36.png");}
    public static ImageIcon addIcon() { return new ImageIcon("com.cn55.marvel/src/img/add_white_36.png");}
    public static ImageIcon addIconDisabled() { return new ImageIcon("com.cn55.marvel/src/img/add_grey_36.png");}
    public static ImageIcon datePickerIcon() { return new ImageIcon("com.cn55.marvel/src/img/event_white_36.png");}
}
