// Generated by esidl 0.2.1.

package org.w3c.dom;

public interface DOMImplementation
{
    // DOMImplementation
    public boolean hasFeature(String feature, String version);
    public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId);
    public Document createDocument(String namespace, String qualifiedName, DocumentType doctype);
    public Document createHTMLDocument(String title);
}
