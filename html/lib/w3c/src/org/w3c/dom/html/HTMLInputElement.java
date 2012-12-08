// Generated by esidl 0.2.1.

package org.w3c.dom.html;

import org.w3c.dom.NodeList;
import org.w3c.dom.file.FileList;

public interface HTMLInputElement extends HTMLElement
{
    // HTMLInputElement
    public String getAccept();
    public void setAccept(String accept);
    public String getAlt();
    public void setAlt(String alt);
    public String getAutocomplete();
    public void setAutocomplete(String autocomplete);
    public boolean getAutofocus();
    public void setAutofocus(boolean autofocus);
    public boolean getDefaultChecked();
    public void setDefaultChecked(boolean defaultChecked);
    public boolean getChecked();
    public void setChecked(boolean checked);
    public String getDirName();
    public void setDirName(String dirName);
    public boolean getDisabled();
    public void setDisabled(boolean disabled);
    public HTMLFormElement getForm();
    public FileList getFiles();
    public String getFormAction();
    public void setFormAction(String formAction);
    public String getFormEnctype();
    public void setFormEnctype(String formEnctype);
    public String getFormMethod();
    public void setFormMethod(String formMethod);
    public boolean getFormNoValidate();
    public void setFormNoValidate(boolean formNoValidate);
    public String getFormTarget();
    public void setFormTarget(String formTarget);
    public String getHeight();
    public void setHeight(String height);
    public boolean getIndeterminate();
    public void setIndeterminate(boolean indeterminate);
    public HTMLElement getList();
    public String getMax();
    public void setMax(String max);
    public int getMaxLength();
    public void setMaxLength(int maxLength);
    public String getMin();
    public void setMin(String min);
    public boolean getMultiple();
    public void setMultiple(boolean multiple);
    public String getName();
    public void setName(String name);
    public String getPattern();
    public void setPattern(String pattern);
    public String getPlaceholder();
    public void setPlaceholder(String placeholder);
    public boolean getReadOnly();
    public void setReadOnly(boolean readOnly);
    public boolean getRequired();
    public void setRequired(boolean required);
    public int getSize();
    public void setSize(int size);
    public String getSrc();
    public void setSrc(String src);
    public String getStep();
    public void setStep(String step);
    public String getType();
    public void setType(String type);
    public String getDefaultValue();
    public void setDefaultValue(String defaultValue);
    public String getValue();
    public void setValue(String value);
    public long getValueAsDate();
    public void setValueAsDate(long valueAsDate);
    public double getValueAsNumber();
    public void setValueAsNumber(double valueAsNumber);
    public HTMLOptionElement getSelectedOption();
    public String getWidth();
    public void setWidth(String width);
    public void stepUp();
    public void stepUp(int n);
    public void stepDown();
    public void stepDown(int n);
    public boolean getWillValidate();
    public ValidityState getValidity();
    public String getValidationMessage();
    public boolean checkValidity();
    public void setCustomValidity(String error);
    public NodeList getLabels();
    public void select();
    public int getSelectionStart();
    public void setSelectionStart(int selectionStart);
    public int getSelectionEnd();
    public void setSelectionEnd(int selectionEnd);
    public String getSelectionDirection();
    public void setSelectionDirection(String selectionDirection);
    public void setSelectionRange(int start, int end);
    public void setSelectionRange(int start, int end, String direction);
    // HTMLInputElement-17
    public String getAlign();
    public void setAlign(String align);
    public String getUseMap();
    public void setUseMap(String useMap);
}
