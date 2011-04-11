import httplib2
import json

class LingAPIException(Exception):
    pass

class LingClient(object):
    """ A Python client for Ling's JSON API. """

    def __init__(self, hostname, port=8080):
        self.hostname = hostname
        self.port = port

    def _do_post(self, resource, method, data, max, sort):
        payload = {}
        # These can't be empty strings.
        for arg in [resource, method, data]:
            if not arg:
                raise LingAPIException("Empty required parameter: %s" % arg) 
        url = "http://%s:%s/%s/%s/%s" % (self.hostname, self.port, resource, method, sort)
        http = httplib2.Http(".cache")
        payload['string'] = data
        if max:
            payload['count'] = max
        resp, content = http.request(url, "PUT", body=json.dumps(payload),
                                     headers={'content-type': 'application/json'})
        return {
            'response': resp,
            'content': json.loads(content)
        }

    def originality(self, resource, string, max=None, sort=None):
        if sort and sort not in ["asc", "desc"]:
            raise LingAPIException("Bad sort. Use either asc or desc.")
        return self._do_post(resource, "originality", string, max, sort)

    def sort(self, resource, string, max=None, sort=None):
        if sort and sort not in ["asc", "desc"]:
            raise LingAPIException("Bad sort. Use either asc or desc.")
        return self._do_post(resource, "sort", string, max, sort)

    def interesting(self, resource, string, max=None, sort=None):
        if sort and sort not in ["least", "most"]:
            raise LingAPIException("Bad sort. Use either least or most.")
        return self._do_post(resource, "interesting", string, max, sort)
