#!/bin/bash

rm -rf slides.html
rm -rf slides.pdf

marp slides.md -o slides.html
marp slides.md --allow-local-files --jpeg-quality 50 --pdf --pdf-notes -o slides.pdf 