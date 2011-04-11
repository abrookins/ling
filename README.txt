Ling is an API that ranks the originality of strings. It is designed
to use American National Corpus word frequency data, which includes
the ratio of word usage in a corpus of ~20 million words.

The API requires an ANC-token-word.txt file. This is the CSV output
of the ANC's word frequency data (non-tokenized, including both
spoken and written English). The API source includes a function
that will convert the CSV data into a Clojure data structure.

You can download ANC data here:
 - http://www.americannationalcorpus.org/SecondRelease/frequency2.html

In addition to ranking strings for originality, the API can return
a list of sentences or words in a given string sorted by originality.

This project is under active development, largely untested, not
intended for use in production systems, not exactly packaged for
deployment, and is provided without any warranty or implied warranty.
I offer it purely to share the work I have done while learning
Clojure.

Copyright 2011, Andrew Brookins. Released under the Eclipse Public
License, same as Clojure.
