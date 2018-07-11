package com.kooppi.nttca.portal.common.utils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.namespace.QName;

/**
 * For fixing a bug in JAX-RS 2.0 spec.
 * 
 * @see https://java.net/jira/browse/JAX_RS_SPEC-475
 * @see http://kingsfleet.blogspot.hk/2014/05/reading-and-writing-jax-rs-link-objects.html
 */

public class LinkJaxb {
    private URI uri;
    private Map<QName, Object> params;

    public LinkJaxb() {
        this(null, null);
    }

    public LinkJaxb(URI uri) {
        this(uri, null);
    }

    public LinkJaxb(URI uri, Map<QName, Object> map) {
        this.uri = uri;
        this.params = map != null ? map : new HashMap<QName, Object>();
    }

    @XmlAttribute(name = "href")
    public URI getUri() {
        return uri;
    }

    @XmlAnyAttribute
    public Map<QName, Object> getParams() {
        return params;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setParams(Map<QName, Object> params) {
        this.params = params;
    }
}
