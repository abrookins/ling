ling is a web service that will sort and rank words/sentences from
JSON input based on American English word frequency data.

INSTALLING

The project.clj file is a Leiningen project description, with which
you should be able to get dependencies.

Before you run the service, you need to load up some word frequencies.
First, download a CSV file of word frequencies from the American
National Corpus (ANC-token-word.txt):

 - http://www.americannationalcorpus.org/SecondRelease/frequency2.html
 
Place that file in the project root and then start up a REPL and
use the words/load-words function to parse the file, then save the
parsed Clojure data structures on disk with the data/save-data
function.

ling expects to read the file containing these structures from
{project_root}/"words.clj".

USING

The intended use of the API is to request a list of sentences or
words in a sentence (a list of words) sorted by their "originality"
in American English, which is found by looking up the word (or
words) in the ANC data.

You can run a test of the API endpoints using the included suite of
Python smoke tests (test_api.py) which accepts a filename that should
include some words and sentences, e.g. a news article.

CAVEATS

This project is under development, largely untested, and not intended
for use in production systems. It is not packaged for deployment,
and is provided without any warranty or implied warranty. I offer
it purely to share some things I learned while teaching myself
Clojure.

COPYRIGHT

Copyright 2011, Andrew Brookins. Released under the Eclipse Public
License, same as Clojure.
