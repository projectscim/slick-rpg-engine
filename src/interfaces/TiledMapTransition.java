package interfaces;

import org.newdawn.slick.state.transition.Transition;

public interface TiledMapTransition extends Transition {
	
	public void executeAfterCompletion();

}
