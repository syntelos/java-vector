// Generated by esidl 0.2.1.

package org.w3c.dom.html;

import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;

public interface DragEvent extends MouseEvent
{
    // DragEvent
    public DataTransfer getDataTransfer();
    public void initDragEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, Object dummyArg, int detailArg, int screenXArg, int screenYArg, int clientXArg, int clientYArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, short buttonArg, EventTarget relatedTargetArg, DataTransfer dataTransferArg);
}
