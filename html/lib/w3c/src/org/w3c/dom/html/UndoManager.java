// Generated by esidl 0.2.1.

package org.w3c.dom.html;

public interface UndoManager
{
    // UndoManager
    public int getLength();
    public Object item(int index);
    public int getPosition();
    public int add(Object data, String title);
    public void remove(int index);
    public void clearUndo();
    public void clearRedo();
}