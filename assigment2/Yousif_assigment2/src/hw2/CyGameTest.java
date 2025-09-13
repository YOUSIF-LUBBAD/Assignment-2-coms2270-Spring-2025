package hw2;

public class CyGameTest {
	public static void main(String args[]) {
		CyGame game = new CyGame(16, 200);
		System.out.println("Expected start is Player 1*: (0, false, $200) Player 2: (0, false, $200)");
		System.out.println(game);
		System.out.println();
		
		// Print the board square types
		System.out.println("Board squares: ");
		System.out.println(game.getSquareType(0) + " expect 0");
		System.out.println(game.getSquareType(1) + " expect 5");
		System.out.println(game.getSquareType(2) + " expect 5");
		System.out.println(game.getSquareType(3) + " expect 4");
		System.out.println(game.getSquareType(4) + " expect 5");
		System.out.println(game.getSquareType(5) + " expect 2");
		System.out.println(game.getSquareType(6) + " expect 4");
		System.out.println(game.getSquareType(7) + " expect 3");
		System.out.println(game.getSquareType(8) + " expect 5");
		System.out.println(game.getSquareType(9) + " expect 4");
		System.out.println(game.getSquareType(10) + " expect 2");
		System.out.println(game.getSquareType(11) + " expect 3");
		System.out.println(game.getSquareType(12) + " expect 4");
		System.out.println(game.getSquareType(13) + " expect 5");
		System.out.println(game.getSquareType(14) + " expect 3");
		System.out.println(game.getSquareType(15) + " expect 1");
		System.out.println();
		
		// Player 1 to a PASS_CLASS square
		game.roll(2);
		System.out.println("Expect Player 1 to advance to sqaure 2 + 4 = 6.");
		System.out.println(game);
		System.out.println();
		
		// Player 2 to PAY_RENT square
		game.roll(5);
		System.out.println("Expect Player 1 money $280.");
		System.out.println("Expect Player 2 money $120.");
		System.out.println(game);
		System.out.println();
		
		// Player 1 is now on BLIZZARD, roll an even value
		game.roll(6);
		System.out.println("Expect Player 1 on sqaure 6 (not moved, stuck in blizzard).");
		System.out.println(game);
		System.out.println();

		// Player 2 to FALL_BEHIND
		game.roll(2);
		System.out.println("Expect Player 2 is on square 6.");
		System.out.println(game);
		System.out.println();
		
		// Player 1 gets out of blizzard by rolling an odd value
		// and lands on FALL_BEHIND which result in PAY_RENT
		game.roll(5);
		System.out.println("Expect Player 1 is on square 10.");
		System.out.println("Expect Player 1 money $200.");
		System.out.println("Expect Player 2 money $200.");
		System.out.println(game);
		System.out.println();
		
		// Player 2 stays stuck in blizzard
		// Player 1 to CyTown square and buys CyTown
		game.roll(2);
		game.roll(5);
		game.buyCyTown();
		System.out.println("Expect Player 1 is on square 15.");
		System.out.println("Expect Player 1 money $0.");
		System.out.println("Expect Player 1 owns CyTown (true).");
		System.out.println(game);
		System.out.println();

		// Player 2 stays stuck in blizzard 
		// Player 1 passes over the ENDZONE
		game.roll(4);
		game.roll(4);
		System.out.println("Expect Player 1 is on square 3.");
		System.out.println("Expect Player 1 money $200.");
		System.out.println(game);
		System.out.println();
		
		// Player 2 gets unstuck from the blizzard and end on PAY_RENT
		// The rent payment is now double for player 2
		game.roll(5);
		System.out.println("Expect Player 1 has $360");
		System.out.println("Expect Player 2 has $40");
		System.out.println(game);
		System.out.println();
		
		// Player 1 pays rent again and the game is over
		game.roll(5);
		game.roll(1);
		System.out.println("Expect game over.");
		System.out.println(game);
		System.out.println("Is game ended: " + game.isGameEnded());
	}
}
