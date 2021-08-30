package games.dicemonastery.actions;

import games.dicemonastery.DiceMonasteryGameState;
import games.dicemonastery.components.Monk;
import games.dicemonastery.components.Pilgrimage;

import static games.dicemonastery.DiceMonasteryConstants.ActionArea.GATEHOUSE;
import static java.util.stream.Collectors.toList;

public class GoOnPilgrimage extends UseMonk {

    public final Pilgrimage.DESTINATION destination;

    public GoOnPilgrimage(Pilgrimage.DESTINATION destination, int piety) {
        super(piety);
        this.destination = destination;
    }

    @Override
    public boolean _execute(DiceMonasteryGameState state) {
        Monk pilgrim = state.monksIn(GATEHOUSE, state.getCurrentPlayer()).stream()
                .filter(m -> m.getPiety() == actionPoints).collect(toList()).get(0);
        state.startPilgrimage(destination, pilgrim);
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GoOnPilgrimage) {
            return ((GoOnPilgrimage) obj).destination == destination && ((GoOnPilgrimage) obj).actionPoints == actionPoints;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return actionPoints + destination.ordinal() * 1481 - 168729;
    }

    @Override
    public String toString() {
        return String.format("Pilgrimage to %s (piety %d)", destination.toString(), actionPoints);
    }
}
