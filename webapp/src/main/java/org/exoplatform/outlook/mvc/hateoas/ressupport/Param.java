package org.exoplatform.outlook.mvc.hateoas.ressupport;

/**
 * The Param.
 */
public class Param {

    /**
     * The Name.
     */
    String name;

    /**
     * The Value.
     */
    Object value;

    /**
     * Instantiates a new Param.
     *
     * @param name  the name
     * @param value the value
     */
    public Param(String name, Object value) {
    this.name = name;
    this.value = value;
  }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
    return name;
  }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
    this.name = name;
  }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Object getValue() {
    return value;
  }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Object value) {
    this.value = value;
  }
}
