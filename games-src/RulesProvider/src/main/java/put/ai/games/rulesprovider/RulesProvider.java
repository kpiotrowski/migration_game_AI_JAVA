package put.ai.games.rulesprovider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import put.ai.games.engine.BoardFactory;
import put.ai.games.migration.MigrationBoardFactory;

public class RulesProvider {

    public static final RulesProvider INSTANCE = new RulesProvider();
    private List<BoardFactory> rules;

    private RulesProvider() {
        BoardFactory[] games = new BoardFactory[0];
        //NOTE if using bash script you will only leave games that do not throw NoSuchMethodException
        //you will get compliation error
        //for Aries my workaround was to change AriesBoardFactory
        //to use the same code as MagnetonBoardFactory, so then it also possibly throws NoSuchMethodException
        try {
            games = new BoardFactory[]{
                new MigrationBoardFactory(),
            };
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(RulesProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        rules = Collections.unmodifiableList(Arrays.asList(games));
    }

    public List<BoardFactory> getAvailablesRules() {
        return rules;
    }

    public BoardFactory getRulesByName(String name) {
        for (BoardFactory f : rules) {
            if (name.equalsIgnoreCase(f.getName())) {
                return f;
            }
        }
        return null;
    }
}
