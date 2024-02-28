package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        addKnightMove(board, position, position.getRow() - 2, position.getColumn() - 1, moves);
        addKnightMove(board, position, position.getRow() - 2, position.getColumn() + 1, moves);
        addKnightMove(board, position, position.getRow() + 2, position.getColumn() - 1, moves);
        addKnightMove(board, position, position.getRow() + 2, position.getColumn() + 1, moves);
        addKnightMove(board, position, position.getRow() - 1, position.getColumn() - 2, moves);
        addKnightMove(board, position, position.getRow() - 1, position.getColumn() + 2, moves);
        addKnightMove(board, position, position.getRow() + 1, position.getColumn() - 2, moves);
        addKnightMove(board, position, position.getRow() + 1, position.getColumn() + 2, moves);

        return moves;
    }

    private void addKnightMove(ChessBoard board, ChessPosition from, int toRow, int toColumn, Collection<ChessMove> moves) {
        ChessPosition to = new ChessPosition(toRow, toColumn);
        if (toRow >= 1 && toRow <= 8 && toColumn >= 1 && toColumn <= 8) {
            if (board.getPiece(to) == null || board.getPiece(to).getTeamColor() != board.getPiece(from).getTeamColor()) {
                moves.add(new ChessMove(from, to, null));
            }
        }
    }
}