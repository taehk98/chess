package chess.PieceMovesCalculators;

import chess.*;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    ChessPiece.PieceType[] pieceTypes = {
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.QUEEN
    };
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        if(position.getRow() == 2 &&
                board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition oneSqPos = new ChessPosition(position.getRow()+1 , position.getColumn());
            if(board.getPiece(oneSqPos) == null){moves.add(new ChessMove(position, oneSqPos, null));}
            ChessPosition twoSqPos = new ChessPosition(position.getRow()+2 , position.getColumn());
            if(board.getPiece(twoSqPos) == null && board.getPiece(oneSqPos) == null){moves.add(new ChessMove(position, twoSqPos, null));}
            ChessPosition leftDiagPos = new ChessPosition(position.getRow()+1 , position.getColumn()-1);
            if(position.getColumn()-1 >= 1 && board.getPiece(leftDiagPos) != null
                    && (board.getPiece(leftDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                moves.add(new ChessMove(position, leftDiagPos, null));}
            ChessPosition rightDiagPos = new ChessPosition(position.getRow()+1 , position.getColumn()+1);
            if(position.getColumn()+1 <= 8 && board.getPiece(rightDiagPos) != null
                    && (board.getPiece(rightDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                moves.add(new ChessMove(position, rightDiagPos, null));}}
        if(position.getRow() == 7
                && board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK){
            ChessPosition oneSqPos = new ChessPosition(position.getRow()-1 , position.getColumn());
            if(board.getPiece(oneSqPos) == null){
                moves.add(new ChessMove(position, oneSqPos, null));}
            ChessPosition twoSqPos = new ChessPosition(position.getRow()-2 , position.getColumn());
            if(board.getPiece(twoSqPos) == null && board.getPiece(oneSqPos) == null){
                moves.add(new ChessMove(position, twoSqPos, null));}
            ChessPosition leftDiagPos = new ChessPosition(position.getRow()-1 , position.getColumn()+1);
            if(position.getColumn()+1 <= 8 && board.getPiece(leftDiagPos) != null
                    && (board.getPiece(leftDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                moves.add(new ChessMove(position, leftDiagPos, null));}
            ChessPosition rightDiagPos = new ChessPosition(position.getRow()-1 , position.getColumn()-1);
            if(position.getColumn()-1 >= 8 && board.getPiece(rightDiagPos) != null
                    && (board.getPiece(rightDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                moves.add(new ChessMove(position, rightDiagPos, null));}}
        if((position.getRow() == 3 || position.getRow() == 4 || position.getRow() == 5
                || position.getRow() == 6 || position.getRow() == 7) && board.getPiece(position).getTeamColor()
                == ChessGame.TeamColor.WHITE){
            ChessPosition oneSqPos = new ChessPosition(position.getRow()+1 , position.getColumn());
            if(board.getPiece(oneSqPos) == null){
                if(position.getRow() == 7){
                    ChessMove[] newMoves = {
                            new ChessMove(position, oneSqPos, ChessPiece.PieceType.BISHOP),
                            new ChessMove(position, oneSqPos, ChessPiece.PieceType.KNIGHT),
                            new ChessMove(position, oneSqPos, ChessPiece.PieceType.ROOK),
                            new ChessMove(position, oneSqPos, ChessPiece.PieceType.QUEEN)};
                    moves.addAll(Arrays.asList(newMoves));
                } else moves.add(new ChessMove(position, oneSqPos, null));}
            ChessPosition leftDiagPos = new ChessPosition(position.getRow()+1 , position.getColumn()-1);
            if(position.getColumn()-1 >= 1 && (board.getPiece(leftDiagPos) != null
                    && (board.getPiece(leftDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor()))){
                if (position.getRow() == 7) {
                    for (ChessPiece.PieceType pieceType : pieceTypes) {
                        moves.add(new ChessMove(position, leftDiagPos, pieceType));}
                } else moves.add(new ChessMove(position, leftDiagPos, null));}
            ChessPosition rightDiagPos = new ChessPosition(position.getRow()+1 , position.getColumn()+1);
            if(position.getColumn()+1 <= 8 && (board.getPiece(rightDiagPos) != null
                    && (board.getPiece(rightDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor()))) {
                if (position.getRow() == 7) {
                    for (ChessPiece.PieceType pieceType : pieceTypes) {
                        moves.add(new ChessMove(position, rightDiagPos, pieceType));}
                } else moves.add(new ChessMove(position, rightDiagPos, null));}}
        if((position.getRow() == 3 || position.getRow() == 4 || position.getRow() == 5
                || position.getRow() == 6 || position.getRow() == 2) && board.getPiece(position).getTeamColor()
                == ChessGame.TeamColor.BLACK){
            ChessPosition oneSqPos = new ChessPosition(position.getRow()-1 , position.getColumn());
            if(board.getPiece(oneSqPos) == null){
                if(position.getRow() == 2){
                    for (ChessPiece.PieceType pieceType : pieceTypes) {
                        moves.add(new ChessMove(position, oneSqPos, pieceType));}}
                else moves.add(new ChessMove(position, oneSqPos, null));}
            ChessPosition leftDiagPos = new ChessPosition(position.getRow()-1 , position.getColumn()+1);
            if(position.getColumn()+1 <= 8 && board.getPiece(leftDiagPos) != null
                    && (board.getPiece(leftDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())){
                if(position.getRow() == 2){
                    for (ChessPiece.PieceType pieceType : pieceTypes) {
                        moves.add(new ChessMove(position, leftDiagPos, pieceType));}}
                else moves.add(new ChessMove(position, leftDiagPos, null));}
            ChessPosition rightDiagPos = new ChessPosition(position.getRow()-1 , position.getColumn()-1);
            if(position.getColumn()-1 >= 1 && board.getPiece(rightDiagPos) != null
                    && (board.getPiece(rightDiagPos).getTeamColor()
                    != board.getPiece(position).getTeamColor())) {
                if(position.getRow() == 2){
                    for (ChessPiece.PieceType pieceType : pieceTypes) {
                        moves.add(new ChessMove(position, rightDiagPos, pieceType));}}
                else moves.add(new ChessMove(position, rightDiagPos, null));}}
        return moves;}
}
