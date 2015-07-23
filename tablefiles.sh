#!/bin/bash

egrep '[a-z]' TableOfContents.wiki | sed 's/[][\*]//g; s/^ *//; s/ .*//'
