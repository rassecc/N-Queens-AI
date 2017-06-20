# N-Queens-AI

This program solves the N-Queens problem using straight-forward steepest-ascent hill climbing and also with the genetic algorithm.

///////////////////////////////////////////////////////////////////////

Enter a choice
1) Run the Straight-forward steepest-ascent hill climbing algortihm
2) Run the genetic algorithm
3) Exit the program

Your choice: 

///////////////////////////////////////////////////////////////////////


1. You enter 1 to run the hill climbing algorithm.
	1. Enter size of the board you want to work with 
		- if you enter 8, the board will be 8x8

	2. Enter how many puzzles to be created to test

	3. Enter true or false on whether you want the soultion steps for every board
		1. if true, every step is printed for each board along with searchcost, number of moves to complete, and run time
		2. if false, the average of search cost, number of moves to complete, and runtime is calculated over all tests and printed



2. You enter 2 to run the genetic algorithm
	1. Enter size of the board you want to work with 
		- if you enter 8, the board will be 8x8

	2.  Enter how many puzzles to be created to test

	3.  Enter a number for how big the initial population
		- if enter 1, will crash since we need at least 2 nodes to be mates

	4.  Enter a a number between 0.1 and 1.0 that will be taken as the mutation probability

	5.  Enter 1 or 2 if you want to see the final solutions of the boards or just averages
		- if 1, the final state of all boards will be printed and you get run time, moves for each board, and search cost
		- if 2, the average of all runtimes, moves, and searchcost of all boards is calculated and displayed



3. You enter 3 to exit the program
	1. Program closed
