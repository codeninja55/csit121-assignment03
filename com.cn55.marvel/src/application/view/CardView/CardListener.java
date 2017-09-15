package application.view.CardView;

import java.util.EventListener;

public interface CardListener extends EventListener {
    void formActionOccurred(CardEvent e);
}
