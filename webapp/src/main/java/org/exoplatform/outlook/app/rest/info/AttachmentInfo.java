package org.exoplatform.outlook.app.rest.info;

import org.exoplatform.outlook.jcr.File;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.*;

/**
 * The type Attachment info.
 */
public class AttachmentInfo extends GeneralInfoBox {

  // TODO protected fields is better in general, this will let a freedom to extend the class easily
  private String comment;

  private String ewsUrl;

  private String userEmail;

  private String userName;

  private String messageId;

  private String attachmentToken;

  /**
   * Instantiates a new Attachment info.
   *
   * @param metadata the metadata
   * @param links the links
   * @param comment the comment
   * @param ewsUrl the ews url
   * @param userEmail the user email
   * @param userName the user name
   * @param messageId the message id
   * @param attachmentToken the attachment token
   * @param files the files
   */
  public AttachmentInfo(PagedResources.PageMetadata metadata,
                        List<Link> links,
                        String comment,
                        String ewsUrl,
                        String userEmail,
                        String userName,
                        String messageId,
                        String attachmentToken,
                        List<File> files) {

    set_metadata(new Metadata(metadata));
    add(links);

    setComment(comment);
    setEwsUrl(ewsUrl);
    setUserEmail(userEmail);
    setUserName(userName);
    setMessageId(messageId);
    setAttachmentToken(attachmentToken);

    List<FileInfo> fileInfos = new LinkedList<>();
    files.forEach((f) -> {
      fileInfos.add(new FileInfo(f));
    });

    Collection<Children> contextParams = new ArrayList(); // TODO generics?
    contextParams.add(new Children(fileInfos));

    set_embedded(new EmbeddedContent(contextParams));
  }

  /**
   * Gets comment.
   *
   * @return the comment
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets comment.
   *
   * @param comment the comment
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Gets ews url.
   *
   * @return the ews url
   */
  public String getEwsUrl() {
    return ewsUrl;
  }

  /**
   * Sets ews url.
   *
   * @param ewsUrl the ews url
   */
  public void setEwsUrl(String ewsUrl) {
    this.ewsUrl = ewsUrl;
  }

  /**
   * Gets user email.
   *
   * @return the user email
   */
  public String getUserEmail() {
    return userEmail;
  }

  /**
   * Sets user email.
   *
   * @param userEmail the user email
   */
  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Sets user name.
   *
   * @param userName the user name
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Sets message id.
   *
   * @param messageId the message id
   */
  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  /**
   * Gets attachment token.
   *
   * @return the attachment token
   */
  public String getAttachmentToken() {
    return attachmentToken;
  }

  /**
   * Sets attachment token.
   *
   * @param attachmentToken the attachment token
   */
  public void setAttachmentToken(String attachmentToken) {
    this.attachmentToken = attachmentToken;
  }

  // TODO private?
  private class Children {

    private List<FileInfo> files;

    /**
     * Instantiates a new Children.
     *
     * @param fileInfos the file infos
     */
    public Children(List<FileInfo> fileInfos) {
      files = fileInfos;
    }

    /**
     * Gets files.
     *
     * @return the files
     */
    public List<FileInfo> getFiles() {
      return files;
    }

    /**
     * Sets files.
     *
     * @param files the files
     */
    public void setFiles(List<FileInfo> files) {
      this.files = files;
    }
  }
}
