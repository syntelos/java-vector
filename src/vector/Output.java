package vector;

/**
 * Output fast overlay on a double buffered scene.  Requires flush on
 * {@link Component user} resize.
 * 
 * @see Display
 * @see Offscreen
 */
public final class Output
    extends Object
{
    /**
     * Overlay following Init exclusively
     */
    public enum Require {
        Init, Overlay, Scene;
    }


    private Offscreen offscreen;

    private Require require = Require.Init;


    public Output(){
        super();
    }


    public void completedScene(){

        this.require = Require.Init;
    }
    public void completedOverlay(){

        this.require = Require.Init;
    }
    public void requestScene(){

        this.require = Require.Scene;
    }
    public void requestOverlay(){

        if (Require.Init == this.require){

            this.require = Require.Overlay;
        }
    }
    public boolean requireOverlay(){

        return (Require.Overlay == this.require);
    }
    public Offscreen offscreen(Component component){

        if (null == this.offscreen){

            this.offscreen = new Offscreen(component);
        }
        return this.offscreen;
    }
    public void flush(){

        Offscreen offscreen = this.offscreen;
        if (null != offscreen){
            this.offscreen = null;
            try {
                offscreen.flush();
            }
            catch (Exception ignore){
            }
        }
        this.require = Require.Init;
    }
}
