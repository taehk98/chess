package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int temptRow;
        temptRow = position.getRow();
        for (int i = position.getColumn()+1; i <= 8; i++) {
            temptRow--;
            if (temptRow < 1) {
                break;
            }
            ChessPosition pos = new ChessPosition(temptRow, i);
            if (pos.getRow() <= 8 && pos.getRow() >= 1 && board.getPiece(pos) == null) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            } // When there is no piece, add the move
            else if (board.getPiece(pos) != null && (
                    board.getPiece(pos).getTeamColor() == board.getPiece(position).getTeamColor())) {
                break;
            } // When there is one of my pieces
            else if (board.getPiece(pos) != null && (board.getPiece(pos).getTeamColor()
                    != board.getPiece(position).getTeamColor())) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } // When there is an enemy's piece, add the move and end the loop;
        } // bottom right diagonal
        
        temptRow = position.getRow();
        for (int i = position.getColumn()+1; i <= 8; i++) {
            temptRow++;
            if (temptRow > 8) {
                break;
            }
            ChessPosition pos2 = new ChessPosition(temptRow, i);
            if (pos2.getRow() <= 8 && pos2.getRow() >= 1
                    && board.getPiece(pos2) == null) {
                ChessMove move = new ChessMove(position, pos2, null);
                moves.add(move);
            } // When there is no piece, add the move
            else if (board.getPiece(pos2) != null && (
                    board.getPiece(pos2).getTeamColor() ==
                            board.getPiece(position).getTeamColor())) {

                break;
            } // When there is one of my pieces
            else if (board.getPiece(pos2) != null &&
                    (board.getPiece(pos2).getTeamColor()
                            != board.getPiece(position).getTeamColor())) {
                ChessMove move = new ChessMove(position, pos2, null);
                moves.add(move);
                break;
            } // When there is an enemy's piece, add the move and end the loop
        }// top right diagonal

        temptRow = position.getRow();
        for (int i = position.getColumn()-1; i >=1; i--) {
            temptRow--;
            if (temptRow < 1) {
                break;
            }
            ChessPosition pos3 = new ChessPosition(temptRow, i);
            if (pos3.getRow() >= 1 && pos3.getRow() <= 8 && board.getPiece(pos3) == null) {
                ChessMove move = new ChessMove(position, pos3, null);
                moves.add(move);
            } // When there is no piece, add the move
            else if (board.getPiece(pos3) != null && (
                    board.getPiece(pos3).getTeamColor() == board.getPiece(position).getTeamColor())) {
                break;
            } // When there is one of my pieces
            else if (board.getPiece(pos3) != null && (board.getPiece(pos3).getTeamColor() != board.getPiece(position).getTeamColor())) {
                ChessMove move = new ChessMove(position, pos3, null);
                moves.add(move);
                break;
            } // When there is an enemy's piece, add the move and end the loop;
        } // bottom left diagonal

        temptRow = position.getRow();
        for (int i = position.getColumn()-1; i >= 1; i--) {
            temptRow++;
            if (temptRow > 8) {
                break;
            }
            ChessPosition pos4 = new ChessPosition(temptRow, i);
            if (pos4.getRow() <= 8 && pos4.getRow() >= 1 && board.getPiece(pos4) == null) {
                ChessMove move = new ChessMove(position, pos4, null);
                moves.add(move);
            } // When there is no piece, add the move
            else if (board.getPiece(pos4) != null && (
                    board.getPiece(pos4).getTeamColor() == board.getPiece(position).getTeamColor())) {
                break;
            } // When there is one of my pieces
            else if (board.getPiece(pos4) != null && (board.getPiece(pos4).getTeamColor() != board.getPiece(position).getTeamColor())) {
                ChessMove move = new ChessMove(position, pos4, null);
                moves.add(move);
                break;
                // When there is an enemy's piece, add the move and end the loop;
            } // top left diagonal
        }
        return moves;
    }
}
