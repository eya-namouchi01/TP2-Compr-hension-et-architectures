package hai913i.tp2;

import java.util.List;
import java.util.Map;

/**
Single entry point for the facts used by the rest of the program.
 **/

public interface FactsInterface {
    List<String> classNames();
    Map<String, String> methodOwners();
    List<Facts.CallArc> calls();

}
