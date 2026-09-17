package org.indra.medicalreportingservice.adapter.out.mongodb.document;

public class AttachmentViewDocument {

    public String attachmentId;
    public String fileName;

    public AttachmentViewDocument() {
    }

    public AttachmentViewDocument(String attachmentId, String fileName) {
        this.attachmentId = attachmentId;
        this.fileName = fileName;
    }
}
