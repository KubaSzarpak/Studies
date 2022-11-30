/**
 * @author Szarpak Jakub S25511
 */

package UTP21;


public interface Selector<T> { // Uwaga: interfejs musi być sparametrtyzowany

    boolean select(T value);
}
