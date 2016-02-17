/*
 * Copyright 2016 Ivo Woltring <WebMaster@ivonet.nl>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.ivonet.service.service;


import nl.ivonet.service.boundary.RandomSingleton;
import nl.ivonet.service.boundary.TinyUrl;
import nl.ivonet.service.model.Tiny;
import nl.ivonet.service.model.Token;
import nl.ivonet.service.model.Tokens;
import org.apache.commons.validator.UrlValidator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_ACCEPTABLE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/")
@Stateless
public class TinyService {

    @PersistenceContext(unitName = "tinyPU")
    private EntityManager em;

    @Inject
    private TinyUrl tinyUrl;

    @Inject
    private Logger logger;

    @Context
    private UriInfo uriInfo;

    @Context
    private ServletContext context;

    @GET
    @Produces(TEXT_HTML)
    public String index() {
        return getWebappResource("index.html");
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") final String token) {
        try {
            final Tiny tiny = this.em.createNamedQuery("Tiny.findById", Tiny.class)
                                     .setParameter("id", this.tinyUrl.decode(token))
                                     .getSingleResult();
            this.em.createNamedQuery("Tiny.updateCounter")
                   .setParameter("id", tiny.getId())
                   .executeUpdate();
            return Response.temporaryRedirect(URI.create(tiny.getUrl()))
                           .build();

        } catch (final NoResultException e) {
            throw new WebApplicationException(NOT_FOUND);
        }
    }

    @POST
    @Produces(TEXT_HTML)
    public Response form(@FormParam("url") final String url) {
        if (url.isEmpty()) {
            throw new WebApplicationException(NO_CONTENT);
        }
        if (isWrongUrl(url)) {
            throw new WebApplicationException(NOT_ACCEPTABLE);
        }
        if (isUrlFromMyDomain(url)) {
            throw new WebApplicationException(FORBIDDEN);
        }

        final URI uri = createUrl(url);

        return Response.ok(getWebappResource("index.html").replace("http://ivo2u.nl", uri.toString()))
                       .build();
    }

    private boolean isUrlFromMyDomain(final String url) {
        return url.contains(this.uriInfo.getBaseUri()
                                        .getHost());
    }

    @POST
    @Path("/api")
    @Produces(TEXT_PLAIN)
    public Response api(@FormParam("url") final String url) {
        if (url.isEmpty() || isWrongUrl(url) || isUrlFromMyDomain(url)) {
            return Response.ok("The request was wrong in some way... Please try again.")
                           .build();
        }

        final URI uri = createUrl(url);

        return Response.ok(uri.toString())
                       .build();
    }

    @POST
    @Path("/api")
    @Produces(TEXT_PLAIN)
    @Consumes(TEXT_PLAIN)
    public Response post(final String url) {
        if (url.isEmpty() || isWrongUrl(url) || isUrlFromMyDomain(url)) {
            return Response.ok("The request was wrong in some way... Please try again.")
                           .build();
        }

        final URI uri = createUrl(url);

        return Response.ok(uri.toString())
                       .build();
    }

    @GET
    @Path("/api/{token}")
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public Token token(@PathParam("token") final String token) {
        final int decoded = this.tinyUrl.decode(token);

        try {
            final Tiny tiny = this.em.createNamedQuery("Tiny.findById", Tiny.class)
                                     .setParameter("id", decoded)
                                     .getSingleResult();

            return new Token(makeUrl(tiny.getId()), tiny);
        } catch (NoResultException e) {
            return new Token();
        }

    }

    @GET
    @Path("/popular")
    @Produces(APPLICATION_JSON)
    public Response popular() {
        final List<Tiny> resultList = this.em.createNamedQuery("Tiny.popular", Tiny.class)
                                             .setMaxResults(5)
                                             .getResultList();
        final Tokens tokens = new Tokens();
        resultList.stream()
                  .map(p -> new Token(makeUrl(p.getId()), p))
                  .forEach(tokens::add);
        return Response.ok(tokens)
                       .build();
    }

    @GET
    @Path("/lucky")
    @Produces(APPLICATION_JSON)
    public Response lucky() {
        try {
            final Integer max = this.em.createNamedQuery("Tiny.maxId", Integer.class)
                                       .getSingleResult();
            final RandomSingleton random = RandomSingleton.getInstance();
            final Tiny tiny = this.em.createNamedQuery("Tiny.lucky", Tiny.class)
                                     .setParameter("seed", random.random(max))
                                     .setMaxResults(1)
                                     .getSingleResult();
            return Response.temporaryRedirect(URI.create(tiny.getUrl()))
                           .build();
        } catch (final NoResultException e) {
            throw new WebApplicationException(NOT_FOUND);
        }
    }



    private boolean isWrongUrl(final String url) {
        final String[] schemes = {"http", "https"};
        final UrlValidator urlValidator = new UrlValidator(schemes);
        return !urlValidator.isValid(url);
    }

    private URI createUrl(final String url) {
        final List<Tiny> tinies = this.em.createNamedQuery("Tiny.findByUrl", Tiny.class)
                                         .setParameter("url", url)
                                         .getResultList();
        final Tiny tiny;
        if (tinies.isEmpty()) {
            tiny = new Tiny();
            tiny.setUrl(url);
            this.em.persist(tiny);
            this.em.flush();
        } else {
            tiny = tinies.get(0);
        }

        return makeUrl(tiny.getId());
    }

    private URI makeUrl(final Integer id) {
        return UriBuilder.fromPath("http://" + this.uriInfo.getBaseUri()
                                                           .getHost())
                         .path(this.tinyUrl.encode(id))
                         .build();
    }

    public String getWebappResource(final String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(this.context.getRealPath("/"), filename)));
        } catch (final IOException e) {
            return "http://ivo2u.nl";
        }
    }

}
