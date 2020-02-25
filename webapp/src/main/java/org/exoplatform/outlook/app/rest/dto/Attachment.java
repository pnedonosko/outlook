package org.exoplatform.outlook.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.*;

/**
 * The type Attachment.
 */
public class Attachment extends AbstractFileResource {

  protected final String comment;

  protected final String ewsUrl;

  protected final String userEmail;

  protected final String userName;

  protected final String messageId;

  protected final String attachmentToken;

  /**
   * Instantiates a new Attachment.
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
  public Attachment(PagedResources.PageMetadata metadata,
                    List<Link> links,
                    String comment,
                    String ewsUrl,
                    String userEmail,
                    String userName,
                    String messageId,
                    String attachmentToken,
                    List<org.exoplatform.outlook.jcr.File> files) {

    set_metadata(new Metadata(metadata));
    add(links);

    this.comment = comment;
    this.ewsUrl = ewsUrl;
    this.userEmail = userEmail;
    this.userName = userName;
    this.messageId = messageId;
    this.attachmentToken = attachmentToken;

    List<File> fileDTOS = new LinkedList<>();
    files.forEach((f) -> {
      fileDTOS.add(new File(f));
    });

    Collection<Children> contextParams = new ArrayList<>();
    contextParams.add(new Children(fileDTOS));

    set_embedded(new EmbeddedContent(contextParams));
  }

  /**
   * Gets comment.
   *
   * @return the comment
   */
  @JsonProperty("comment")
  public String getComment() {
    return comment;
  }

  /**
   * Gets ews url.
   *
   * @return the ews url
   */
  @JsonProperty("ewsUrl")
  public String getEwsUrl() {
    return ewsUrl;
  }

  /**
   * Gets user email.
   *
   * @return the user email
   */
  @JsonProperty("userEmail")
  public String getUserEmail() {
    return userEmail;
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  @JsonProperty("userName")
  public String getUserName() {
    return userName;
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  @JsonProperty("messageId")
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets attachment token.
   *
   * @return the attachment token
   */
  @JsonProperty("attachmentToken")
  public String getAttachmentToken() {
    return attachmentToken;
  }

  protected class Children {

    protected final List<File> files;

    /**
     * Instantiates a new Children.
     *
     * @param files the file infos
     */
    public Children(List<File> files) {
      this.files = files;
    }

    /**
     * Gets files.
     *
     * @return the files
     */
    public List<File> getFiles() {
      return files;
    }
  }
}
