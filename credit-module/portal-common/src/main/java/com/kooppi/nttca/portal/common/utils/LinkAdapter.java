package com.kooppi.nttca.portal.common.utils;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

/**
 * For fixing a bug in JAX-RS 2.0 spec.
 * 
 * @see https://java.net/jira/browse/JAX_RS_SPEC-475
 * @see http://kingsfleet.blogspot.hk/2014/05/reading-and-writing-jax-rs-link-objects.html
 */
public class LinkAdapter extends XmlAdapter<LinkJaxb, Link> {

    public LinkAdapter() {
    }

    public Link unmarshal(LinkJaxb p1) {

        Link.Builder builder = Link.fromUri(p1.getUri());
        for (Map.Entry<QName, Object> entry : p1.getParams().entrySet()) {
            builder.param(entry.getKey().getLocalPart(), entry.getValue().toString());
        }
        return builder.build();
    }

    public LinkJaxb marshal(Link p1) {

        Map<QName, Object> params = new HashMap<>();
        for (Map.Entry<String, String> entry : p1.getParams().entrySet()) {
            params.put(new QName("", entry.getKey()), entry.getValue());
        }
        return new LinkJaxb(p1.getUri(), params);
    }
}
