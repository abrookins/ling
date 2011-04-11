In its present state, ling is a web service that will sort and rank
words from JSON input text based on the frequency of their usage
in American English.

The project.clj file is a Leiningen project description, with which
you should be able to get dependencies.

Before you run the service, you must first start up a REPL and
parse a word frequency list from the American National Corpus
(ANC-token-word.txt) using the load-words function and save the
parsed data on disk in the form of Clojure data structures with the
save-data function.

The CSV output of the ANC's word frequency data that ling expects
to parse (non-tokenized, including both spoken and written English)
is freely available. You can download ANC data here:

 - http://www.americannationalcorpus.org/SecondRelease/frequency2.html

ling expects to read a file containing these structures from
"words.clj" after which it parses any words it receives at the API
endpoints in core.clj and sorts them based on the ANC data for any
word matches found in the input text. Sorting is based on some weird
and probably wrong math I concocted.

In addition to ranking strings for originality, the API can return
a list of sentences or words in a given string sorted by the
"originality" of the words (or sentences) in American English,
according to the corpus.

This project is under development, largely untested, not intended
for use in production systems, not packaged for deployment, and is
provided without any warranty or implied warranty.  I offer it
purely to share the work I have done while learning Clojure.

Copyright 2011, Andrew Brookins. Released under the Eclipse Public
License, same as Clojure.
