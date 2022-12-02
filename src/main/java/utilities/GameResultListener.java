package utilities;

import core.AbstractGameState;
import core.CoreConstants;
import core.Game;
import core.actions.AbstractAction;
import core.interfaces.IComponentContainer;
import evaluation.GameListener;
import core.interfaces.IStatisticLogger;
import evaluation.metrics.Event;

import java.util.*;

public class GameResultListener extends GameListener {

    Map<String, Object> collectedData = new LinkedHashMap<>();

    public GameResultListener(IStatisticLogger logger) {
        super(logger, null);
    }

    public GameResultListener() {
        super(null, null);
    }

    @Override
    public void onEvent(Event event)
    {
        if(event.type == Event.GameEvent.GAME_OVER) {
            AbstractGameState state = event.state;
            collectedData.put("Game", game.getGameType().name());
            collectedData.put("GameID", state.getGameID());
            collectedData.put("Players", state.getNPlayers());
            collectedData.put("Rounds", state.getTurnOrder().getRoundCounter());
            collectedData.put("Turns", state.getTurnOrder().getTurnCounter());
            collectedData.put("Ticks", game.getTick());
            for (int p = 0; p < 9; p++) {
                if (p >= state.getNPlayers()) {
                    collectedData.put(String.format("P%d_Score", p), 0);
                    collectedData.put(String.format("P%d_Ordinal", p), 0);
                    collectedData.put(String.format("P%d_Type", p), "");
                } else {
                    collectedData.put(String.format("P%d_Score", p), state.getGameScore(p));
                    collectedData.put(String.format("P%d_Ordinal", p), state.getOrdinalPosition(p));
                    collectedData.put(String.format("P%d_Type", p), game.getPlayers().get(p).toString());
                }
            }
            logger.record(collectedData);
            collectedData = new HashMap<>();
        }
    }

    /**
     * Returns the total number of components in the state as the first element of the returned value
     * and an array of the counts that are hidden to each player
     * <p>
     *
     * @param state
     * @return The total number of components
     */
    private Pair<Integer, int[]> countComponents(AbstractGameState state) {
        int[] hiddenByPlayer = new int[state.getNPlayers()];
        // we do not include containers in the count...just the lowest-level items
        // open to debate on this. But we are consistent across State Size and Hidden Information stats
        int total = (int) state.getAllComponents().stream().filter(c -> !(c instanceof IComponentContainer)).count();
        for (int p = 0; p < hiddenByPlayer.length; p++)
            hiddenByPlayer[p] = state.getUnknownComponentsIds(p).size();
        return new Pair<>(total, hiddenByPlayer);
    }

}
