package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> moves = new ArrayList<>();

        //up
        for(int i = position.getRow()+1; i <= 8; i++){

            ChessPosition pos = new ChessPosition(i, position.getColumn());
            if(board.getPiece(pos) == null) {
                moves.add(new ChessMove(position, pos, null));
            } else if(board.getPiece(pos) != null
                    && (board.getPiece(pos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                moves.add(new ChessMove(position, pos, null));
                break;
            } else if (board.getPiece(pos) != null
                    && (board.getPiece(pos).getTeamColor()
                    == board.getPiece(position).getTeamColor())) {
                break;
            }
        }

        //down
        for(int i = position.getRow()-1; i >= 1; i--){
            ChessPosition pos = new ChessPosition(i, position.getColumn());
            if(board.getPiece(pos) == null) {
                moves.add(new ChessMove(position, pos, null));
            }else if(board.getPiece(pos) != null
                    && (board.getPiece(pos).getTeamColor()
                    != board.getPiece(position).getTeamColor())) {
                moves.add(new ChessMove(position, pos, null));
                break;
            }else if ((board.getPiece(pos) != null
                    && (board.getPiece(pos).getTeamColor()
                    == board.getPiece(position).getTeamColor()))) {
                break;
            }
        }

        //right
        for(int j = position.getColumn()+1; j <= 8; j++){
            ChessPosition pos = new ChessPosition(position.getRow(), j);
            if(board.getPiece(pos) == null) {
                moves.add(new ChessMove(position, pos, null));
            }else if(board.getPiece(pos) != null
                    && (board.getPiece(pos).getTeamColor()
                    != board.getPiece(position).getTeamColor())) {
                moves.add(new ChessMove(position, pos, null));
                break;
            }else if ((board.getPiece(pos) != null
                    && (board.getPiece(pos).getTeamColor()
                    == board.getPiece(position).getTeamColor()))) {
                break;
            }
        }

        //left
        for(int k = position.getColumn()-1; k >= 1 ; k--){
            ChessPosition pos = new ChessPosition(position.getRow(), k);
            if(board.getPiece(pos) == null) {
                moves.add(new ChessMove(position, pos, null));
            }else if(board.getPiece(pos) != null
                    && (board.getPiece(pos).getTeamColor()
                    != board.getPiece(position).getTeamColor())) {
                moves.add(new ChessMove(position, pos, null));
                break;
            }else if ((board.getPiece(pos) != null
                    && (board.getPiece(pos).getTeamColor()
                    == board.getPiece(position).getTeamColor()))) {
                break;
            }
        }
        return moves;
    }

}
