""" 
A basic Python client for use with the ling API.

- Finds the originality of a word or sentence
- Sorts words or sentences by their originality
- Finds N most or least interesting words or sentences in a string
"""

import httplib2
import json

class LingAPIException(Exception):
    pass

class LingClient:
    """
    A Python client for Ling's JSON API.
    """

    def __init__(self, hostname="localhost", port=8080):
        self.hostname = hostname
        self.port = port

    def _do_post(self, resource, method, data, max, sort_direction, sort_opts=None):
        """
        Send a JSON payload with a string for ranking or sorting.
        """
        payload = {}
        response = {} 
        http = httplib2.Http(".cache")
        # These can't be empty strings.
        for arg in [resource, method, data]:
            if not arg:
                raise LingAPIException("Empty required parameter: %s" % arg) 
        payload['string'] = data
        # Ensure the sort direction, if it exists, is valid.
        if sort_direction and sort_direction not in sort_opts:
            raise LingAPIException("Bad sort. Use one of: %s" % ''.join([`opt` for opt in sort_opts]))
        url = "http://%s:%s/%s/%s" % (self.hostname, self.port, resource, method)
        if sort_direction:
            url = "%s/%s" % (url, sort_direction)
        if max:
            payload['count'] = max
        resp, content = http.request(url, "PUT", body=json.dumps(payload),
                                     headers={'content-type': 'application/json'})
        if resp.status == 200:
            response["response"]  = resp
            response["content"] = json.loads(content)
        else:
            raise LingAPIException("Response failed with status code: %s" % resp["status"])
        return response

    def originality(self, resource, string):
        """
        Find the originality of resources in a string. 
        """
        return self._do_post(resource, "originality", string, max=None, sort_direction=None)

    def sort(self, resource, string, max=None, sort_direction="desc"):
        """
        Chunk and sort the resources string.
        """
        return self._do_post(resource, "sort", string, max, sort_direction,
                             sort_opts=["asc", "desc"])

    def interesting(self, resource, string, max=None, sort_direction="least"): 
        """
        Chunk and find the most interesting resources in a string.
        """
        return self._do_post(resource, "interesting", string, max, sort_direction,
                             ["least", "most"])
