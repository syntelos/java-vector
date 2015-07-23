The **vector** [git](http://git-scm.com/) repository maintains its current best code on its _master_ branch.  A
```sh

git clone http://code.google.com/p/java-vector/
```
or
```sh

http get http://java-vector.googlecode.com/git/
```
from the repository identifies the current best (production) version.

## Open ##

A package version is _opened_ with the first use of a version number in a commit to the [build.version](http://code.google.com/p/java-vector/source/browse/build.version) file.

## Close ##

A package version is _closed_ with the first commit to another version number in the [build.version](http://code.google.com/p/java-vector/source/browse/build.version) file.

## Tags ##

Each open and close point is tagged with "open-" _version-after_ and "close-" _version-before_.

For example, [open-1.0.3](http://code.google.com/p/java-vector/source/browse/?name=open-1.0.3)

## Branches ##

The stable `master` branch is fed from unstable version branches named "b" _version_, for example [b1.0.3](http://code.google.com/p/java-vector/source/browse/?name=b1.0.3).
