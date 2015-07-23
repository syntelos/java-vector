#!/bin/bash

ls ../vector/src/vector/*.java | egrep -v '/(Blink|Bounds|Color|Component|Debug|Dimension|Event|Font|Frame|Offscreen|Output|Padding|Stroke|TableCell|Transform)'

