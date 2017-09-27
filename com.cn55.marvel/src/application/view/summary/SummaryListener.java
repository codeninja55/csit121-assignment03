package application.view.summary;
import java.util.EventListener;
public interface SummaryListener extends EventListener{
    void refreshActionPerformed(SummaryView e);
}
