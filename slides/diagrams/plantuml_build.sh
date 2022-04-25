#!/bin/bash

rm -rf *.png
plantuml .

mv *.png ../images