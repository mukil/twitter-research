package org.deepamehta.twitter;

import javax.ws.rs.core.Response.Status;

/**
 * A very basic client for researching with the public Twitter Search API v1.1 and DeepaMehta 4.1.2
 *
 * @author Malte Reißig (<malte@mikromedia.de>)
 * @website https://github.com/mukil/twitter-research
 * @version 1.2
 *
 */

public class TwitterAPIException extends Throwable {

    private Status status;
    private String message;

    public TwitterAPIException (String message, Status status) {
        this.status = status;
        this.message = message;
    }

    public TwitterAPIException (String message) {
        this.status = Status.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

}
