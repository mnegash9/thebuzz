package edu.lehigh.cse216.team24.backend;

/**
 * SimpleRequest provides a format for clients to present an idea string to the
 * server.
 * 
 * @param mIdea The idea being provided by the client.
 */
public record SimpleRequest(String mIdea, String link, String file) {
}
