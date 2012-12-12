// Generated by esidl 0.2.1.

package org.w3c.dom.html;

public interface HTMLFormElement extends HTMLElement
{
    // HTMLFormElement
    public String getAcceptCharset();
    public void setAcceptCharset(String acceptCharset);
    public String getAction();
    public void setAction(String action);
    public String getAutocomplete();
    public void setAutocomplete(String autocomplete);
    public String getEnctype();
    public void setEnctype(String enctype);
    public String getEncoding();
    public void setEncoding(String encoding);
    public String getMethod();
    public void setMethod(String method);
    public String getName();
    public void setName(String name);
    public boolean getNoValidate();
    public void setNoValidate(boolean noValidate);
    public String getTarget();
    public void setTarget(String target);
    public HTMLFormControlsCollection getElements();
    public int getLength();
    public Object getElement(int index);
    public Object getElement(String name);
    public void submit();
    public void reset();
    public boolean checkValidity();
}