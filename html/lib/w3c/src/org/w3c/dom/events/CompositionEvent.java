// Generated by esidl 0.2.1.

package org.w3c.dom.events;

import org.w3c.dom.html.Window;

public interface CompositionEvent extends UIEvent
{
    // CompositionEvent
    public String getData();
    public String getLocale();
    public void initCompositionEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, Window viewArg, String dataArg, String localeArg);
}