// Generated by esidl 0.2.1.

package org.w3c.dom.html;

import org.w3c.dom.events.Event;

public interface PageTransitionEvent extends Event
{
    // PageTransitionEvent
    public boolean getPersisted();
    public void initPageTransitionEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, boolean persistedArg);
}