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

        for(int i = position.getRow()-2; i <= position.getRow()+2; i += 2) {
            if(i != position.getRow() && i <= 8 && i >= 1){
                ChessPosition pos = new ChessPosition(i, position.getColumn()-1);
                if(position.getColumn() - 1 >= 1){
                    if (board.getPiece(pos) == null || (board.getPiece(pos) != null
                            && (board.getPiece(pos).getTeamColor()
                            != board.getPiece(position).getTeamColor()))) {
                        moves.add(new ChessMove(position, pos, null));
                    }
                }

                pos = new ChessPosition(i, position.getColumn()+1);
                if(position.getColumn() + 1 <= 8) {
                    if (board.getPiece(pos) == null || (board.getPiece(pos) != null
                            && (board.getPiece(pos).getTeamColor()
                            != board.getPiece(position).getTeamColor()))) {
                        moves.add(new ChessMove(position, pos, null));
                    }
                }
            }
        }

        for(int j = position.getColumn()-2; j <= position.getColumn()+2; j += 2){
            if(j != position.getColumn() && j <= 8 && j >= 1){
                ChessPosition pos = new ChessPosition(position.getRow()-1, j);
                if(position.getRow() -1 >= 1){
                    if(board.getPiece(pos) == null || (board.getPiece(pos) != null
                            && (board.getPiece(pos).getTeamColor()
                            != board.getPiece(position).getTeamColor()))){
                        moves.add(new ChessMove(position, pos, null));
                    }
                }

                pos = new ChessPosition( position.getRow()+1, j);
                if(position.getRow() +1 <= 8){
                    if(board.getPiece(pos) == null || (board.getPiece(pos) != null
                            && (board.getPiece(pos).getTeamColor()
                            != board.getPiece(position).getTeamColor()))){
                        moves.add(new ChessMove(position, pos, null));
                    }
                }
            }
        }
        return moves;
    }

}
