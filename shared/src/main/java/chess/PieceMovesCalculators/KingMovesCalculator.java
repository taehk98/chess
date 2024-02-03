package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int upRow = position.getRow() + 1;
        int downRow = position.getRow() - 1;

        // possible moves on the current row + 1
        if(upRow <= 8) {
            ChessPosition middleTopMove = new ChessPosition(upRow, position.getColumn());
            if(board.getPiece(middleTopMove) == null ||
                    (board.getPiece(middleTopMove) != null && (board.getPiece(middleTopMove).getTeamColor()
                    != board.getPiece(position).getTeamColor()))){
                moves.add(new ChessMove(position, middleTopMove, null));
            }
            if(position.getColumn()-1 >= 1) {
                ChessPosition leftTopMove = new ChessPosition(upRow, position.getColumn()-1);
                if(board.getPiece(leftTopMove) == null ||
                        (board.getPiece(leftTopMove) != null && (board.getPiece(leftTopMove).getTeamColor()
                                != board.getPiece(position).getTeamColor()))){
                    moves.add(new ChessMove(position, leftTopMove, null));
                }
            }
            if(position.getColumn()+1 <= 8) {
                ChessPosition rightTopMove = new ChessPosition(upRow, position.getColumn()+1);
                if(board.getPiece(rightTopMove) == null ||
                        (board.getPiece(rightTopMove) != null && (board.getPiece(rightTopMove).getTeamColor()
                                != board.getPiece(position).getTeamColor()))){
                    moves.add(new ChessMove(position, rightTopMove, null));
                }
            }
        }

        // possible moves on the current row - 1
        if (downRow >= 1) {
            ChessPosition middleBottomMove = new ChessPosition(downRow, position.getColumn());
            if(board.getPiece(middleBottomMove) == null ||
                    (board.getPiece(middleBottomMove) != null && (board.getPiece(middleBottomMove).getTeamColor()
                            != board.getPiece(position).getTeamColor()))){
                moves.add(new ChessMove(position, middleBottomMove, null));
            }
            if(position.getColumn()-1 >= 1) {
                ChessPosition leftBottomMove = new ChessPosition(downRow, position.getColumn()-1);
                if(board.getPiece(leftBottomMove) == null ||
                        (board.getPiece(leftBottomMove) != null && (board.getPiece(leftBottomMove).getTeamColor()
                                != board.getPiece(position).getTeamColor()))){
                    moves.add(new ChessMove(position, leftBottomMove, null));
                }
            }
            if(position.getColumn()+1 <= 8) {
                ChessPosition rightBottomMove = new ChessPosition(downRow, position.getColumn()+1);
                if(board.getPiece(rightBottomMove) == null ||
                        (board.getPiece(rightBottomMove) != null && (board.getPiece(rightBottomMove).getTeamColor()
                                != board.getPiece(position).getTeamColor()))){
                    moves.add(new ChessMove(position, rightBottomMove, null));
                }
            }
        }

        // possible moves on the same row
        if(position.getColumn()-1 >= 1){
            ChessPosition leftMove = new ChessPosition(position.getRow(), position.getColumn()-1);
            if(board.getPiece(leftMove) == null || (board.getPiece(leftMove) != null && (board.getPiece(leftMove).getTeamColor()
                    != board.getPiece(leftMove).getTeamColor()))) {
                moves.add(new ChessMove(position, leftMove, null));
            }
        }
        if(position.getColumn()+1 <= 8){
            ChessPosition rightMove = new ChessPosition(position.getRow(), position.getColumn()+1);
            if(board.getPiece(rightMove) == null || (board.getPiece(rightMove) != null && (board.getPiece(rightMove).getTeamColor()
                    != board.getPiece(rightMove).getTeamColor()))) {
                moves.add(new ChessMove(position, rightMove, null));
            }
        }
        return moves;
    }
}

