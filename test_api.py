import sys

from ling_client import LingClient

def open_file(filename):
    f = open(filename)
    return f.read().replace("\n", "")

# argument: filename
def main(argv):
    test_string = open_file(argv[0])
    ling = LingClient()

    print "Originality of word 'color':"
    print ling.originality("words", "color")
    
    print "Originality of word 'parochial':"
    print ling.originality("words", "parochial")

    print "Words sorted ascending:"
    print ling.sort("words", test_string, limit=100, direction="asc")

    print "Words sorted descending:"
    print ling.sort("words", test_string, limit=100, direction="desc")

    print "Sentences sorted ascending:"
    print ling.sort("sentences", test_string, limit=5, direction="asc")

    print "Sentences sorted descending:"
    print ling.sort("sentences", test_string, limit=5, direction="desc")

if __name__ == "__main__":
    main(sys.argv[1:])
