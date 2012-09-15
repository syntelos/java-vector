package vector;

/**
 * Components implementing this interface need to have their {@link
 * Component#modified() modified} method called after subsequent
 * components are added.
 * 
 * @see Grid
 */
public interface TailInit
    extends Component
{}
