""" 
A basic Python client for use with the ling API.

- Find the originality of a word or sentence
- Sort words or sentences by their originality with an optional limit
"""

import httplib2

try:
    import cjson as json
except ImportError:
    try:
        import simplejson as json
    except ImportError:
        import json

class LingAPIException(Exception):
    pass

class LingClient:
    """
    A Python client for the ling JSON API.
    """
    base_url = "http://{hostname}:{port}/{string_part}/{method}"

    def __init__(self, hostname="localhost", port=8080):
        self.hostname = hostname
        self.port = port

    def _do_post(self, url, data, limit=None):
        """
        Send a JSON payload with a string for ranking or sorting.
        """
        payload = {}
        response = {} 
        http = httplib2.Http(".cache")

        # Data can't be an empty string.
        if not data:
            raise LingAPIException("Empty required parameter: %s" % arg) 

        payload['string'] = data
        if limit:
            payload['limit'] = limit

        resp, content = http.request(url, "PUT", body=json.dumps(payload),
                                     headers={'content-type': 'application/json'})
        if resp.status == 200:
            response["response"]  = resp
            response["content"] = json.loads(content)
        else:
            print url
            raise LingAPIException("Response failed with status code: %s" % resp["status"])

        return response

    def originality(self, string_part, string):
        """
        Find the originality of parts (words or sentences) of a string.
        """
        url = self.base_url.format(hostname=self.hostname, port=self.port,
                                   string_part=string_part, method="originality")
        return self._do_post(url, string)

    def sort(self, string_part, string, direction="desc", limit=None):
        """
        Sort the parts (word or sentence) of a string based on the
        originality of the parts.
        """
        valid_sort_directions = ["asc", "desc"]
        if direction not in valid_sort_directions:
            raise LingAPIException("Bad sort direction. Use one of: %s"
                    % ''.join([`opt` for opt in valid_sort_directions]))

        url = self.base_url.format(hostname=self.hostname,
                                 port=self.port, string_part=string_part,
                                 method="sort")
        url = "{url}/{direction}".format(url=url, direction=direction)

        return self._do_post(url, string, limit)

