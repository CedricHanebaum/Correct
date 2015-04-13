#Correct!

As a Matse homework corrector I often have to search manually for the homework of my groups in all the homeworks I've downloaded from the Ilias. Because this is a quite
tedious work I wanted to automate this process. The ultimate goal of this Software is to turn a zip file with the homework of all students into an Eclipse project, only
containing the groups to correct. Started as a standalone project, it soon became obvious, that to programmatically create an eclipse project, I had to write an eclipse 
plugin.

##Features:
* Import wizard for files downloaded from the Ilias.
* Add groups and groupmembers.
* Save groups and groupmembers in a preferences file.
* Create an eclipse project for each homework.
* Create a package for each group.
* Automatically find and extract the homework of the first groupmember (how turned in homework) into the groups package.
* Automatically add the package decleration to each java file.

##Planed Features:
* Delete/Edit Groups/ Groupmembers.

##Contact:
If you wish to contribute, ask for a feature or send a bug report, feel free to contact me at <Cedric.Hanebaum@rwth-aachen.de>

