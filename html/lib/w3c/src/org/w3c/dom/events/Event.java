// Generated by esidl 0.2.1.

package org.w3c.dom.events;

public interface Event
{
    // Event
    public String getType();
    public EventTarget getTarget();
    public EventTarget getCurrentTarget();
    public static final short CAPTURING_PHASE = 1;
    public static final short AT_TARGET = 2;
    public static final short BUBBLING_PHASE = 3;
    public short getEventPhase();
    public void stopPropagation();
    public void stopImmediatePropagation();
    public boolean getBubbles();
    public boolean getCancelable();
    public void preventDefault();
    public boolean getDefaultPrevented();
    public boolean getIsTrusted();
    public long getTimeStamp();
    public void initEvent(String type, boolean bubbles, boolean cancelable);
}
