# Equi
Equilateral embedding of polyhedra

**Notes** 

Because of complications with Java 11 the current version cannot (yet) be built
with sbt and must be built with IntelliJ IDEA. You need the following
libraries:

* be.ugent.caagt.graph3d
* be.ugent.caagt.fxlogwindow
* be.ugent.caagt.perm (version 1.1)
* org.ejml:ejml-ddense:0.34

To run the jar, you also need the JavaFX `lib` directory to be in your
`LD_LIBRARY_PATH`.