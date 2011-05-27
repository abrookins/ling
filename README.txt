ling is a web service that will sort and rank words and sentences
from JSON input based on word frequency data from a language corpus.

INSTALLING

The project.clj file is a Leiningen project description, with which
you should be able to get dependencies.

ling expects word frequency data to exist in the database specified
in the conf file (see: "CONFIGURING" for more on configuration).
If the database doesn't exist on launch, ling will attempt to
bootstrap itself by downloading the word frequency file, parsing
it, and loading it into the database.

CONFIGURING

You can set configuration details such as the name of the file in
which to find raw words and word frequency ratios taken from a
language corpus, the function used to parse values from that file,
and the database in which to store the parsed results in
src/ling/conf.clj.

The default configuration is to parse ANC data from the file
"ANC-token-word.txt", store it in memory and persist it as a Clojure
data structure to {project_root}/db.clj. 

USING

You should then be able to run the service with Leiningen by executing:

  lein run -m ling.launcher

A (very basic) sample Python client is included as ling_client.py.

The intended use of the API is to request a list of words or sentences
in a string sorted by the frequency of the words' usage in a language
corpus. A lower usage frequency in the corpus makes a word more 
"interesting."

For example, using ANC data, the word "dog" has an originality score of
-9.374997232279057 while the word "bleep" has an originality of
-14.141435592390513. Thus "bleep" is more original in American
English.

Likewise, the sentence "I walked the dog" has an originality score
of -6.654624194914662, making it not very original, whereas at
-10.80417439765554 the sentence "General Ford forded the fjord" is
starting to get hot.

In addition to originality, ling also returns the accuracy of a
score, with 1 representing 100% accuracy and 0 representing 0%
accuracy for the given string. At 100% accuracy, there was a frequency
ratio in the corpus for every word parsed from the string, whereas
at 0% there were no frequencies for any of the words.

TESTING

You can run a "test" of the API endpoints using the included suite
of Python smoke tests (test_api.py) which accepts a filename that
should include some words and sentences, e.g. the text of a news
article.

The test suite uses the included Python client (ling_client.py) to
run HTTP requests that exercise ling's API, so you will need to
first launch ling.

CAVEATS

This project is under development, largely untested, and not intended
for use in production systems. It is not packaged for deployment,
and is provided without any warranty or implied warranty. I offer
it purely to share some things I learned while teaching myself
Clojure.

COPYRIGHT

Copyright 2011, Andrew Brookins. Released under the Eclipse Public
License, same as Clojure.
