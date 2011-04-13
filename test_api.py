import sys

from test_client import LingClient

HOST = "localhost"

def open_file(filename):
    f = open(filename)
    return f.read().replace("\n", "")

# argument: filename
def main(argv):
    test_string = open_file(argv[0])
    ling = LingClient(HOST)

    print "Originality of word 'color':"
    print ling.originality("words", "color")
    
    print "Originality of word 'parochial':"
    print ling.originality("words", "parochial")

    print "Words sorted ascending:"
    print ling.sort("words", test_string, max=100, sort_direction="asc")

    print "Words sorted descending:"
    print ling.sort("words", test_string, max=100, sort_direction="desc")

    print "Sentences sorted ascending:"
    print ling.sort("sentences", test_string, max=5, sort_direction="asc")

    print "Sentences sorted descending:"
    print ling.sort("sentences", test_string, max=5, sort_direction="desc")

    print "Most interesting 100 words:"
    print ling.interesting("words", test_string, max=100, sort_direction="most")

    print "Least interesting 100 words:"
    print ling.interesting("words", test_string, max=100, sort_direction="least")

    print "Most interesting 2 sentences:"
    print ling.interesting("sentences", test_string, max=2, sort_direction="most")

    print "Least interesting 2 sentences:"
    print ling.interesting("sentences", test_string, max=2, sort_direction="least")


if __name__ == "__main__":
    main(sys.argv[1:])
