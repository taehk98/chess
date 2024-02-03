package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[9][9];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()][position.getColumn()] = piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    public void deletePiece(ChessPosition position) {
        squares[position.getRow()][position.getColumn()] = null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[9][9];
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;

        for(int i = 1; i <= 8; i++) {
            squares[2][i] = new ChessPiece(white, ChessPiece.PieceType.PAWN);
            squares[7][i] = new ChessPiece(black, ChessPiece.PieceType.PAWN);

            if(i == 1 || i == 8) {
                squares[1][i] = new ChessPiece(white, ChessPiece.PieceType.ROOK);
                squares[8][i] = new ChessPiece(black, ChessPiece.PieceType.ROOK);
            }
            if(i == 2 || i == 7) {
                squares[1][i] = new ChessPiece(white, ChessPiece.PieceType.KNIGHT);
                squares[8][i] = new ChessPiece(black, ChessPiece.PieceType.KNIGHT);
            }
            if(i == 3 || i == 6) {
                squares[1][i] = new ChessPiece(white, ChessPiece.PieceType.BISHOP);
                squares[8][i] = new ChessPiece(black, ChessPiece.PieceType.BISHOP);
            }
            if(i == 4) {
                squares[1][i] = new ChessPiece(white, ChessPiece.PieceType.QUEEN);
                squares[8][i] = new ChessPiece(black, ChessPiece.PieceType.QUEEN);
            }
            if(i == 5) {
                squares[1][i] = new ChessPiece(white, ChessPiece.PieceType.KING);
                squares[8][i] = new ChessPiece(black, ChessPiece.PieceType.KING);
            }
        }
    }
}
