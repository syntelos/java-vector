// Generated by esidl 0.2.1.

package org.w3c.dom.file;

import org.w3c.dom.typedarray.ArrayBuffer;

public interface FileReaderSync
{
    // FileReaderSync
    public ArrayBuffer readAsArrayBuffer(Blob blob);
    public String readAsBinaryString(Blob blob);
    public String readAsText(Blob blob);
    public String readAsText(Blob blob, String encoding);
    public String readAsDataURL(Blob blob);
}