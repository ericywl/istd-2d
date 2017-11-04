INSTRUCTIONS FOR EACH SUBJECT

ALGO
=====
The report for the algorithm is in Algo, named Algo_2d_report.
To run the 2-SAT Solver, follow the steps below.
```
1. Put the CNF files to be tested in  Algo/sat_2d/sampleCNF
2. Use a Java IDE to open the Algo folder as a project
3. Open SATSolver2Test.java under Algo/sat_2d/src/main/java/sat
4. Change the STRING fileName to the CNF file's name with .cnf extension
5. Run the program
```
If the problem is satisfiable, <br/>
the program will print the boolean assignment on the consoleand also writes it to Algo/sat_2d/sampleCNF/<fileName>Bool.txt


COMPSTRUCT
===========
The bkAdder.jsim file contains the netlist for the circuit. <br/>
The test.bc file contains the boolean circuit and is used to generate the CNF file. <br/>
The test.cnf file is for testing the logic of our 8th bit sum output via SAT Solver.


INFOSYS
========
To run the SAT Solver, follow the steps below.
```
1. Put the CNF files to be tested in InfoSys/sat_2d/sampleCNF
2. Use a Java IDE to open the InfoSys folder as a project
3. Open SATSolverTest.java under InfoSys/sat_2d/src/main/java/sat
4. Change the STRING fileName to the CNF file's name with .cnf extension
5. Run the program
```
If the problem is satisfiable, <br/>
the boolean assignment is written in InfoSys/sat_2d/sampleCNF/<fileName>Bool.txt
