// Generated by esidl 0.2.1.

package org.w3c.dom.html;

public interface Navigator
{
    // Navigator
    // NavigatorID
    public String getAppName();
    public String getAppVersion();
    public String getPlatform();
    public String getUserAgent();
    // NavigatorOnLine
    public boolean getOnLine();
    // NavigatorContentUtils
    public void registerProtocolHandler(String scheme, String url, String title);
    public void registerContentHandler(String mimeType, String url, String title);
    // NavigatorStorageUtils
    public void yieldForStorageUpdates();
    // NavigatorUserMedia
    public void getUserMedia(String options, NavigatorUserMediaSuccessCallback successCallback);
    public void getUserMedia(String options, NavigatorUserMediaSuccessCallback successCallback, NavigatorUserMediaErrorCallback errorCallback);
}
