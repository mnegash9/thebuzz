package edu.lehigh.cse216.team24.backend;

/**
 * StructuredResponse provides a common format for success and failure messages,
 * with an optional payload of type Object that can be converted into JSON.
 * 
 * @param mStatus The status of the response, typically "ok" or "error".
 * @param mIdea   An idea string to accompany the status.
 * @param mLink   A link string to show if link has been added
 * @param mFile   A file string to show if a file has been added
 * @param mData   Any JSON-friendly object can be referenced here, allowing the
 *                client to receive additional data.
 */
public record StructuredResponse(String mStatus, String mIdea, String mLink, String mFile, Object mData) {
    /**
     * Constructor for StructuredResponse that sets mStatus to "invalid" if it is
     * not provided.
     * 
     * @param mStatus The status of the response, typically "ok" or "error".
     *                Defaults to "invalid" if null.
     * @param mIdea   The idea string to go along with the status.
     * @param mData   An object with additional data to send to the client, which
     *                can be serialized to JSON.
     */
    public StructuredResponse {
        mStatus = (mStatus != null) ? mStatus : "invalid";
    }
}