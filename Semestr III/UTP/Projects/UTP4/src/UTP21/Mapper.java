/**
 *
 *  @author Szarpak Jakub S25511
 *
 */

package UTP21;


public interface Mapper<T, X> { // Uwaga: interfejs musi być sparametrtyzowany
    X map(T value);

}
