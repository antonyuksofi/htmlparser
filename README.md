This app is dedicated to find the best matching element within the target html file comparing to the element with specified id from original html file.

Input args are optional and respond to:
args[0] - path to original html
args[1] - path to target html
args[2] - id of the element within original html to find in target one

The app prints to the console the info about original element and the path to the resulting one.

The search is based on the comparison of the element from original html to the elements of target html of the same tag name
basing on the equality of attributes values and elements texts.

p.s.:
The project was switched to Maven after one hour of unsuccessful tries to establish the env for gradle.
Logger was switched off due to the same reason.

The code itself is raw enough but working :)
Future steps would be to refactor it a bit (basing on OOP approach and with more usage of Stream API) and to add unit test as well.