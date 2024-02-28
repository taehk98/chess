package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        addMovesInDirection(board, position, moves, 1, 0);  // up
        addMovesInDirection(board, position, moves, -1, 0); // down
        addMovesInDirection(board, position, moves, 0, 1);  // right
        addMovesInDirection(board, position, moves, 0, -1); // left
        addMovesInDirection(board, position, moves, 1, 1);  // top right
        addMovesInDirection(board, position, moves, 1, -1); // top left
        addMovesInDirection(board, position, moves, -1, 1); // bottom right
        addMovesInDirection(board, position, moves, -1, -1);// bottom left

        return moves;
    }

    // 해당 방향으로 이동 가능한 모든 위치를 추가하는 메서드
    private void addMovesInDirection(ChessBoard board, ChessPosition position, Collection<ChessMove> moves, int dr, int dc) {
        int newRow = position.getRow() + dr;
        int newCol = position.getColumn() + dc;
        while (isValidPosition(newRow, newCol)) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPos) == null) {
                moves.add(new ChessMove(position, newPos, null));
            } else {
                if (board.getPiece(newPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, newPos, null));
                }
                break;
            }
            newRow += dr;
            newCol += dc;
        }
    }

    // 유효한 체스 위치인지 확인하는 메서드
    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
