// Generated by esidl 0.2.1.

package org.w3c.dom.html;

public interface VideoTrackList
{
    // VideoTrackList
    public int getLength();
    public VideoTrack getElement(int index);
    public VideoTrack getTrackById(String id);
    public int getSelectedIndex();
    public Function getOnchange();
    public void setOnchange(Function onchange);
}
