# Port of the Scala examples of TeaVM to Scala.js

To run them, first you need to compile them. You will need to install
[sbt](http://www.scala-sbt.org/).

You can compile the JavaScript files with:

    $ cd /path/to/this/directory
    $ sbt
    ...
    > fastOptJS
    ...

Now you can open the `-fastopt.html` file. To compile the fully optimized
version for use in the `.html` files without `-fastopt`, run this:

    > fullOptJS
    ...
