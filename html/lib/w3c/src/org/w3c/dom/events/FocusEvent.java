// Generated by esidl 0.2.1.

package org.w3c.dom.events;

import org.w3c.dom.html.Window;

public interface FocusEvent extends UIEvent
{
    // FocusEvent
    public EventTarget getRelatedTarget();
    public void initFocusEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, Window viewArg, int detailArg, EventTarget relatedTargetArg);
}