package hw2;

/**
 * Model of a Monopoly-like game. Two players take turns
 * rolling dice to move around a board. The game ends
 * when one of the players has at least MONEY_TO_WIN
 * money or when one of the players goes bankrupt by
 * having negative money.
 *
 * @author Yousif Lubbad
 */
public class CyGame {

    /*
     * currentPlayer is either 1 or 2, indicating whose turn it is.
     */
    private int currentPlayer;
    
    /*
     * Each player's position (the square index on the board).
     */
    private int player1Square;
    private int player2Square;

    /*
     * The amount of money each player currently has.
     * Going below 0 means bankruptcy.
     */
    private int player1Money;
    private int player2Money;

    /*
     * cyTownOwner is 0 if no one owns CyTown.
     * If player 1 buys it, this becomes 1.
     * If player 2 buys it, this becomes 2.
     */
    private int cyTownOwner;

    /*
     * gameOver tells us if the game has ended.
     * It becomes true if a player has >= MONEY_TO_WIN,
     * or if a player goes bankrupt (money < 0).
     */
    private boolean gameOver = false;

    /*
     * numSquares is how many squares are on the board.
     * For example, if numSquares is 16, the squares
     * range from 0..15.
     */
    private int numSquares;
    
    /*
     * A boolean to track whether the current player has 
     * already collected the ENDZONE_PRIZE once this turn.
     * We only award the endzone prize once per turn if the
     * player passes or lands on square 0.
     */
    private boolean hasCollectedEndzoneThisTurn;

    /**
     * The endzone square type (square 0).
     */
    public static final int ENDZONE = 0;
    /**
     * The CyTown square type (usually the last square, numSquares - 1).
     */
    public static final int CYTOWN = 1;
    /**
     * The pay rent square type.
     */
    public static final int PAY_RENT = 2;
    /**
     * The fall behind square type (the player moves back one square).
     */
    public static final int FALL_BEHIND = 3;
    /**
     * The blizzard square type, where a player may remain stuck if rolling an even number.
     */
    public static final int BLIZZARD = 4;
    /**
     * The pass class square type (the player moves forward an extra 4 squares).
     */
    public static final int PASS_CLASS = 5;
    /**
     * Points awarded when landing on or passing over the endzone square 
     * (only once per turn).
     */
    public static final int ENDZONE_PRIZE = 200;
    /**
     * The standard rent paid to the other player when landing on a pay-rent square.
     */
    public static final int STANDARD_RENT_PAYMENT = 80;
    /**
     * The cost to buy CyTown.
     */
    public static final int CYTOWN_COST = 200;
    /**
     * The amount of money required to win.
     */
    public static final int MONEY_TO_WIN = 400;

    /**
     * Constructs the game with the given number of squares
     * and the specified starting money for each player.
     *
     * @param numSquares    total number of squares on the board (must be >= 2)
     * @param startingMoney the starting amount of money for both players
     */
    public CyGame(int numSquares, int startingMoney) {
        if (numSquares < 2) {
            throw new IllegalArgumentException("The board must have at least 2 squares.");
        }
        this.numSquares = numSquares;
        this.player1Money = startingMoney;
        this.player2Money = startingMoney;
        this.player1Square = 0;
        this.player2Square = 0;
        this.currentPlayer = 1;  // Player 1 always starts
        this.cyTownOwner = 0;    // CyTown is initially unowned
        this.gameOver = false;   // Game is not over at the start
    }

    /**
     * Attempt to buy CyTown if the current player is on
     * the CyTown square, it isn't owned yet, and the player
     * has enough money. Then the turn ends.
     * 
     * If the purchase conditions are not met, this method does nothing
     * other than ending the turn.
     */
    public void buyCyTown() {
        // Check if current player is on the CyTown square
        if (getPlayerSquare(currentPlayer) == numSquares - 1) {
            // Check if the current player has enough money
            if (getPlayerMoney(currentPlayer) >= CYTOWN_COST) {
                // Check if CyTown is unowned and the game is still active
                if (cyTownOwner == 0 && !gameOver) {
                    cyTownOwner = currentPlayer;

                    // Deduct the CYTOWN_COST from whichever player is buying it
                    if (currentPlayer == 1) {
                        player1Money -= CYTOWN_COST;
                    } else {
                        player2Money -= CYTOWN_COST;
                    }
                }
            }
        }
        // Regardless of success, end the turn afterward
        endTurn();
    }

    /**
     * Ends the current player's turn, switching to the other player.
     */
    public void endTurn() {
        currentPlayer = getOtherPlayer();
    }

    /**
     * Returns which player's turn it is, 1 or 2.
     *
     * @return current player (1 or 2)
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the other player: if current is 1, returns 2;
     * if current is 2, returns 1.
     *
     * @return the other player's number
     */
    public int getOtherPlayer() {
        return (currentPlayer == 2) ? 1 : 2;
    }

    /**
     * Returns the amount of money owned by the specified player.
     *
     * @param player the player number (1 or 2)
     * @return the player's current money, or -1 if invalid player
     */
    public int getPlayerMoney(int player) {
        if (player == 1) {
            return player1Money;
        } else if (player == 2) {
            return player2Money;
        }
        return -1; // Invalid player
    }

    /**
     * Returns which square (index) the specified player is on.
     *
     * @param player the player number (1 or 2)
     * @return the square index for that player, or -1 if invalid player
     */
    public int getPlayerSquare(int player) {
        if (player == 1) {
            return player1Square;
        } else if (player == 2) {
            return player2Square;
        }
        return -1; // Invalid player
    }

    /**
     * Returns the type of the specified square, determined by these rules:
     *   - Square 0 is always ENDZONE.
     *   - The last square (numSquares - 1) is CYTOWN.
     *   - Every 5th square is PAY_RENT.
     *   - Every 7th or 11th square is FALL_BEHIND.
     *   - Every 3rd square is BLIZZARD.
     *   - Otherwise, PASS_CLASS.
     *
     * @param square the index of the square
     * @return the type (one of the constants ENDZONE, CYTOWN, PAY_RENT, etc.)
     */
    public int getSquareType(int square) {
        if (square == 0) return ENDZONE;
        if (square == numSquares - 1) return CYTOWN;
        if (square % 5 == 0) return PAY_RENT;
        if (square % 7 == 0 || square % 11 == 0) return FALL_BEHIND;
        if (square % 3 == 0) return BLIZZARD;
        return PASS_CLASS;
    }

    /**
     * Returns true if the game has ended, meaning one player reached
     * MONEY_TO_WIN or a player has negative money.
     *
     * @return true if the game is over, false if it is still in progress
     */
    public boolean isGameEnded() {
        return gameOver;
    }

    /**
     * Returns true if Player 1 is the CyTown owner.
     *
     * @return true if Player 1 owns CyTown
     */
    public boolean isPlayer1CyTownOwner() {
        return cyTownOwner == 1;
    }

    /**
     * Returns true if Player 2 is the CyTown owner.
     *
     * @return true if Player 2 owns CyTown
     */
    public boolean isPlayer2CyTownOwner() {
        return cyTownOwner == 2;
    }

    /**
     * Called when the current player rolls the die (value from 1 to 6).
     * Actions:
     *  - If the player is on BLIZZARD and rolls an even number, they don't move and end their turn.
     *  - Otherwise, move forward by 'value' squares (wrapping around if necessary).
     *  - If the player passes over square 0 (ENDZONE) or lands exactly on square 0, they get ENDZONE_PRIZE
     *    ONLY once per turn.
     *  - Apply the effect of the new square:
     *      * PAY_RENT -> pay rent
     *      * FALL_BEHIND -> go back one square and apply that square's effect
     *      * PASS_CLASS -> jump forward four squares and apply that square's effect
     *      * BLIZZARD -> note that the player may remain stuck if future rolls are even
     *      * CYTOWN -> the turn does not automatically end; the player can buy or end turn
     *  - If the game has already ended, do nothing.
     *
     * @param value the result of the die roll (1..6)
     */
    public void roll(int value) {
        // If the game has ended, ignore this roll
        if (gameOver) return;

        // We only want to award the Endzone prize once per full turn, not once per "roll" call.
        // But based on the problem statement, each call to roll() here effectively is one player's entire turn
        // unless they land on CyTown. So we reset the prize-collected flag at the start of roll().
        hasCollectedEndzoneThisTurn = false;

        // Current square before moving
        int currentSquare = getPlayerSquare(currentPlayer);

        // If we're on BLIZZARD and roll an even number, we skip movement
        if (getSquareType(currentSquare) == BLIZZARD && value % 2 == 0) {
            endTurn();
            return;
        }

        // Move forward
        int newSquare = (currentSquare + value) % numSquares;

        /*
         * If we haven't yet collected the Endzone prize this turn, check two cases:
         *  1) newSquare < currentSquare => we have passed square 0 in wrapping around.
         *  2) newSquare == 0 => we landed exactly on the ENDZONE square.
         */
        if (!hasCollectedEndzoneThisTurn) {
            if (newSquare < currentSquare
                || (newSquare == currentSquare && value > 0)
                || newSquare == 0) 
            {
                if (currentPlayer == 1) {
                    player1Money += ENDZONE_PRIZE;
                } else {
                    player2Money += ENDZONE_PRIZE;
                }
                hasCollectedEndzoneThisTurn = true;
            }
        }


        // Now actually move the piece
        if (currentPlayer == 1) {
            player1Square = newSquare;
        } else {
            player2Square = newSquare;
        }

        // Apply the effect of landing on the new square
        applySquareEffect();

        /*
         * If we did NOT land on CYTOWN, the turn ends automatically.
         * If we did land on CYTOWN, we wait for buyCyTown() or endTurn().
         */
        if (getSquareType(getPlayerSquare(currentPlayer)) != CYTOWN) {
            endTurn();
        }

        // Check if the game ended due to a player's money >= 400 or <0
        checkGameEnd();
    }

    /**
     * Applies effects based on the current player's square:
     *  - PAY_RENT: pay standard or double rent
     *  - FALL_BEHIND: move the player back one square, then recheck that new square's effect
     *  - PASS_CLASS: move the player forward 4 squares, then recheck effect
     *  - BLIZZARD: (the actual movement restriction is handled in roll())
     *  - CYTOWN, ENDZONE: no extra effect
     */
    private void applySquareEffect() {
        int squareType = getSquareType(getPlayerSquare(currentPlayer));

        // 1) PAY_RENT
        if (squareType == PAY_RENT) {
            // If the other player owns CyTown, rent is doubled
            int rent = (cyTownOwner == getOtherPlayer())
                     ? (STANDARD_RENT_PAYMENT * 2)
                     : STANDARD_RENT_PAYMENT;

            if (currentPlayer == 1) {
                player1Money -= rent;
                player2Money += rent;
            } else {
                player2Money -= rent;
                player1Money += rent;
            }
        }
        // 2) FALL_BEHIND
        else if (squareType == FALL_BEHIND) {
            // Move back by 1
            if (currentPlayer == 1) {
                player1Square = Math.max(0, player1Square - 1);
            } else {
                player2Square = Math.max(0, player2Square - 1);
            }
            // Re-apply effect on that new square (could be pay rent, pass class, etc.)
            applySquareEffect();
        }
        // 3) PASS_CLASS
        else if (squareType == PASS_CLASS) {
            int oldSquare = getPlayerSquare(currentPlayer);
            int newSquare = (oldSquare + 4) % numSquares;

            // If we pass 0 (meaning newSquare < oldSquare) or land exactly on 0,
            // and haven't collected the prize yet, award it now
            if (!hasCollectedEndzoneThisTurn) {
                if (newSquare < oldSquare || newSquare == 0) {
                    if (currentPlayer == 1) {
                        player1Money += ENDZONE_PRIZE;
                    } else {
                        player2Money += ENDZONE_PRIZE;
                    }
                    hasCollectedEndzoneThisTurn = true;
                }
            }

            // Update position
            if (currentPlayer == 1) {
                player1Square = newSquare;
            } else {
                player2Square = newSquare;
            }

            // If the new square is itself PASS_CLASS, do NOT keep chaining
            int newSquareType = getSquareType(getPlayerSquare(currentPlayer));
            if (newSquareType != PASS_CLASS) {
                applySquareEffect();
            }
        }
        // 4) BLIZZARD => no immediate effect here
        // 5) CYTOWN or ENDZONE => no additional effect
    }

    /**
     * Checks if the game should end.
     * Conditions:
     *  - If either player's money >= MONEY_TO_WIN, game ends.
     *  - If either player's money < 0, game ends (bankruptcy).
     */
    private void checkGameEnd() {
        if (!gameOver) {
            if (player1Money >= MONEY_TO_WIN
             || player2Money >= MONEY_TO_WIN
             || player1Money < 0
             || player2Money < 0)
            {
                gameOver = true;
                // We do NOT print anything here because the autograder disallows print statements.
            }
        }
    }

    /**
     * Returns a one-line string representation of the current game state.
     * Example:
     *   Player 1*: (0, false, $200) Player 2: (0, false, $200)
     * The asterisk (*) indicates whose turn it is.
     * The tuple (squareIndex, ownsCyTown, $money) represents:
     *   - the current square index
     *   - whether the player owns CyTown
     *   - the player's money.
     */
    @Override
    public String toString() {
        String fmt = "Player 1%s: (%d, %b, $%d) Player 2%s: (%d, %b, $%d)";
        String player1Turn = "";
        String player2Turn = "";

        // Show an asterisk next to the current player's name
        if (getCurrentPlayer() == 1) {
            player1Turn = "*";
        } else {
            player2Turn = "*";
        }

        return String.format(fmt,
                player1Turn,                 // "*" if it's Player 1's turn
                getPlayerSquare(1),          // Player 1's square index
                isPlayer1CyTownOwner(),      // Whether Player 1 owns CyTown
                getPlayerMoney(1),           // Player 1's money
                player2Turn,                 // "*" if it's Player 2's turn
                getPlayerSquare(2),          // Player 2's square index
                isPlayer2CyTownOwner(),      // Whether Player 2 owns CyTown
                getPlayerMoney(2));          // Player 2's money
    }
}