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

package nl.ivonet.service.config;

import javax.servlet.ServletContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static javax.ws.rs.core.Response.ok;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private ServletContext context;

    @Override
    public Response toResponse(final Throwable t) {
        if (t instanceof WebApplicationException) {
            final WebApplicationException exception = (WebApplicationException) t;
            final int status = exception.getResponse()
                                        .getStatus();
            switch (status) {
                case 204:
                    return ok(getWebappResource("204.html")).build();
                case 403:
                    return ok(getWebappResource("403.html")).build();
                case 404:
                    return ok(getWebappResource("404.html")).build();
                case 406:
                    return ok(getWebappResource("406.html")).build();
                default:
                    return ok(getWebappResource("error.html")).build();
            }

        }
        return ok(getWebappResource("error.html")).build();
    }
    
    public String getWebappResource(final String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(this.context.getRealPath("/"), filename)));
        } catch (final IOException e) {
            return "http://ivo2u.nl";
        }
    }
    

}