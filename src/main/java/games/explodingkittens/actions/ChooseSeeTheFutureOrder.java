package games.explodingkittens.actions;

import core.AbstractGameState;
import core.actions.RearrangeDeckOfCards;
import core.components.PartialObservableDeck;
import core.interfaces.IPrintable;
import games.explodingkittens.ExplodingKittensGameState;
import games.explodingkittens.cards.ExplodingKittenCard;

import java.util.Arrays;


public class ChooseSeeTheFutureOrder extends RearrangeDeckOfCards implements IPrintable {

    public ChooseSeeTheFutureOrder(int deckFrom, int deckTo, int fromIndex, int rearrangeDeck, int[] newCardOrder) {
        super(deckFrom, deckTo, fromIndex, rearrangeDeck, newCardOrder);
    }

    @Override
    public boolean execute(AbstractGameState gs) {
        super.execute(gs);

        PartialObservableDeck<ExplodingKittenCard> drawPile = ((ExplodingKittensGameState)gs).getDrawPile();
        int playerID = gs.getTurnOrder().getCurrentPlayer(gs);

        for (int i = 0; i < newCardOrder.length; i++) {
            for (int j = 0; j < gs.getNPlayers(); j++){
                if (j != playerID) {
                    drawPile.setVisibilityOfComponent(i, j, false);        // other players don't know the order anymore
                } else {
                    drawPile.setVisibilityOfComponent(i, j, true);      // this player knows the order
                }
            }
        }

        gs.setMainGamePhase();
        return true;
    }

    public String toString(){
        return "Chosen card order: " + Arrays.toString(newCardOrder);
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return toString();
    }

    @Override
    public void printToConsole(AbstractGameState gameState) {
        System.out.println(toString());
    }
}
