package org.exoplatform.outlook.app.rest.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * The type General info box.
 */
public abstract class GeneralInfoBox extends ResourceSupport {

  private Metadata        _metadata;

  private EmbeddedContent _embedded;

  /**
   * Instantiates a new General info box.
   */
  public GeneralInfoBox() {
  }

  /**
   * Instantiates a new General info box.
   *
   * @param _metadata the metadata
   * @param content the content
   */
  public GeneralInfoBox(Metadata _metadata, Collection<?> content) {
    this._metadata = _metadata;
    _embedded = new EmbeddedContent(content);
  }

  /**
   * Iterator iterator.
   *
   * @return the iterator
   */
  public Iterator<?> iterator() {
    return this._embedded.content.iterator();
  }

  /**
   * Gets metadata.
   *
   * @return the metadata
   */
  public Metadata get_metadata() {
    return _metadata;
  }

  /**
   * Sets metadata.
   *
   * @param _metadata the metadata
   */
  public void set_metadata(Metadata _metadata) {
    this._metadata = _metadata;
  }

  /**
   * Gets embedded.
   *
   * @return the embedded
   */
  public EmbeddedContent get_embedded() {
    return _embedded;
  }

  /**
   * Sets embedded.
   *
   * @param _embedded the embedded
   */
  public void set_embedded(EmbeddedContent _embedded) {
    this._embedded = _embedded;
  }

  /**
   * The type Metadata.
   */
  protected class Metadata {

    /**
     * The Metadata.
     */
    PagedResources.PageMetadata metadata;

    /**
     * Instantiates a new Metadata.
     *
     * @param metadata the metadata
     */
    public Metadata(PagedResources.PageMetadata metadata) {
      this.metadata = metadata;
    }

    /**
     * Gets metadata.
     *
     * @return the metadata
     */
    @JsonProperty("page")
    public PagedResources.PageMetadata getMetadata() {
      return metadata;
    }

    /**
     * Sets metadata.
     *
     * @param metadata the metadata
     */
    public void setMetadata(PagedResources.PageMetadata metadata) {
      this.metadata = metadata;
    }
  }

  /**
   * The type Embedded content.
   */
  protected class EmbeddedContent {

    private Collection<?> content;

    /**
     * Instantiates a new Embedded content.
     *
     * @param content the content
     */
    public EmbeddedContent(Collection<?> content) {
      this.content = content;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    @XmlAnyElement
    @XmlElementWrapper
    @JsonProperty("children")
    public Collection<?> getContent() {
      return Collections.unmodifiableCollection(this.content);
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(Collection<?> content) {
      this.content = content;
    }
  }
}
