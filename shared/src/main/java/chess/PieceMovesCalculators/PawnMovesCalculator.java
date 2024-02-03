package chess.PieceMovesCalculators;

import chess.*;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        if(position.getRow() == 2 &&
                board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE){

            ChessPosition oneSqPos = new ChessPosition(position.getRow()+1 , position.getColumn());
            if(board.getPiece(oneSqPos) == null){
                moves.add(new ChessMove(position, oneSqPos, null));
            }

            ChessPosition twoSqPos = new ChessPosition(position.getRow()+2 , position.getColumn());
            if(board.getPiece(twoSqPos) == null && board.getPiece(oneSqPos) == null){
                moves.add(new ChessMove(position, twoSqPos, null));
            }

            ChessPosition leftDiagPos = new ChessPosition(position.getRow()+1 , position.getColumn()-1);
            if(position.getColumn()-1 >= 1 && board.getPiece(leftDiagPos) != null
                    && (board.getPiece(leftDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                moves.add(new ChessMove(position, leftDiagPos, null));
            }

            ChessPosition rightDiagPos = new ChessPosition(position.getRow()+1 , position.getColumn()+1);
            if(position.getColumn()+1 <= 8 && board.getPiece(rightDiagPos) != null
                    && (board.getPiece(rightDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                moves.add(new ChessMove(position, rightDiagPos, null));
            }
        }
        if(position.getRow() == 7
                && board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK){

            ChessPosition oneSqPos = new ChessPosition(position.getRow()-1 , position.getColumn());
            if(board.getPiece(oneSqPos) == null){
                moves.add(new ChessMove(position, oneSqPos, null));
            }

            ChessPosition twoSqPos = new ChessPosition(position.getRow()-2 , position.getColumn());
            if(board.getPiece(twoSqPos) == null && board.getPiece(oneSqPos) == null){
                moves.add(new ChessMove(position, twoSqPos, null));
            }

            ChessPosition leftDiagPos = new ChessPosition(position.getRow()-1 , position.getColumn()+1);
            if(position.getColumn()+1 <= 8 && board.getPiece(leftDiagPos) != null
                    && (board.getPiece(leftDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                moves.add(new ChessMove(position, leftDiagPos, null));
            }

            ChessPosition rightDiagPos = new ChessPosition(position.getRow()-1 , position.getColumn()-1);
            if(position.getColumn()-1 >= 8 && board.getPiece(rightDiagPos) != null
                    && (board.getPiece(rightDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                moves.add(new ChessMove(position, rightDiagPos, null));
            }
        }

        if((position.getRow() == 3 || position.getRow() == 4 || position.getRow() == 5
                || position.getRow() == 6 || position.getRow() == 7) && board.getPiece(position).getTeamColor()
                == ChessGame.TeamColor.WHITE){
            ChessPosition oneSqPos = new ChessPosition(position.getRow()+1 , position.getColumn());
            if(board.getPiece(oneSqPos) == null){
                if(position.getRow() == 7){
                    moves.add(new ChessMove(position, oneSqPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, oneSqPos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, oneSqPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, oneSqPos, ChessPiece.PieceType.QUEEN));
                }
                else{
                    moves.add(new ChessMove(position, oneSqPos, null));
                }
            }

            ChessPosition leftDiagPos = new ChessPosition(position.getRow()+1 , position.getColumn()-1);
            if(position.getColumn()-1 >= 1 && (board.getPiece(leftDiagPos) != null
                    && (board.getPiece(leftDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor()))){
                if(position.getRow() == 7){
                    moves.add(new ChessMove(position, leftDiagPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, leftDiagPos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, leftDiagPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, leftDiagPos, ChessPiece.PieceType.QUEEN));
                }
                else {
                    moves.add(new ChessMove(position, leftDiagPos, null));
                }
            }

            ChessPosition rightDiagPos = new ChessPosition(position.getRow()+1 , position.getColumn()+1);
            if(position.getColumn()+1 <= 8 && (board.getPiece(rightDiagPos) != null
                    && (board.getPiece(rightDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor()))) {
                if(position.getRow() == 7){
                    moves.add(new ChessMove(position, rightDiagPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, rightDiagPos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, rightDiagPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, rightDiagPos, ChessPiece.PieceType.QUEEN));
                }
                else {
                    moves.add(new ChessMove(position, rightDiagPos, null));
                }
            }
        }

        if((position.getRow() == 3 || position.getRow() == 4 || position.getRow() == 5
                || position.getRow() == 6 || position.getRow() == 2) && board.getPiece(position).getTeamColor()
                == ChessGame.TeamColor.BLACK){
            ChessPosition oneSqPos = new ChessPosition(position.getRow()-1 , position.getColumn());
            if(board.getPiece(oneSqPos) == null){
                if(position.getRow() == 2){
                    moves.add(new ChessMove(position, oneSqPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, oneSqPos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, oneSqPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, oneSqPos, ChessPiece.PieceType.QUEEN));
                }
                else{
                    moves.add(new ChessMove(position, oneSqPos, null));
                }
            }

            ChessPosition leftDiagPos = new ChessPosition(position.getRow()-1 , position.getColumn()+1);
            if(position.getColumn()+1 <= 8 && board.getPiece(leftDiagPos) != null
                    && (board.getPiece(leftDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                if(position.getRow() == 2){
                    moves.add(new ChessMove(position, leftDiagPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, leftDiagPos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, leftDiagPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, leftDiagPos, ChessPiece.PieceType.QUEEN));
                }
                else {
                    moves.add(new ChessMove(position, leftDiagPos, null));
                }
            }

            ChessPosition rightDiagPos = new ChessPosition(position.getRow()-1 , position.getColumn()-1);
            if(position.getColumn()-1 >= 1 && board.getPiece(rightDiagPos) != null
                    && (board.getPiece(rightDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())) {
                if(position.getRow() == 2){
                    moves.add(new ChessMove(position, rightDiagPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, rightDiagPos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, rightDiagPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, rightDiagPos, ChessPiece.PieceType.QUEEN));
                }
                else {
                    moves.add(new ChessMove(position, rightDiagPos, null));
                }
            }
        }
        return moves;
    }
}
