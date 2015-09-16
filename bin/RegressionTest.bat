@echo off
echo "Deleting testoutput.txt if exist"
IF EXIST testoutput.txt del /F testoutput.txt
pause
echo "Executing java class"
java TextBuddy mytestfile.txt < testinput.txt > testoutput.txt
echo "Comparing expected and current output"
fc /w expected.txt testoutput.txt
echo "Regression Test Done"
pause