package ui;

import chess.*;

import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoardPrinter {

    public ChessBoardPrinter() {
    }

    private String[][] initializeChessBoard(ChessBoard chessBoard, boolean isWhite) {
        // Fill the chess board with pieces
        String[][] squares = new String[10][10];
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(row, col));
                if(piece == null) {
                    if ((row + col) % 2 == 0) {
                        squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.EMPTY + EscapeSequences.TERMINAL_BG_COLOR;

                    }else {
                        squares[row][col]  = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.EMPTY + EscapeSequences.TERMINAL_BG_COLOR;
                    }
                }
                else {
                    if ((row + col) % 2 == 0) {
                        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                            if(piece.getPieceType() == ChessPiece.PieceType.KING) {
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY +  EscapeSequences.WHITE_KING + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_QUEEN + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_ROOK + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_PAWN + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_KNIGHT + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.WHITE_BISHOP + EscapeSequences.TERMINAL_BG_COLOR;
                            }
                        }else {
                            if(piece.getPieceType() == ChessPiece.PieceType.KING) {
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_KING + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_QUEEN + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_ROOK + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_PAWN + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_KNIGHT + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.BLACK_BISHOP + EscapeSequences.TERMINAL_BG_COLOR;
                            }
                        }

                    } else {
                        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                            if(piece.getPieceType() == ChessPiece.PieceType.KING) {
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.WHITE_KING + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.WHITE_QUEEN + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.WHITE_ROOK + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.WHITE_PAWN + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.WHITE_KNIGHT + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.WHITE_BISHOP + EscapeSequences.TERMINAL_BG_COLOR;
                            }
                        }else {
                            if(piece.getPieceType() == ChessPiece.PieceType.KING) {
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.BLACK_KING + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.BLACK_QUEEN + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.BLACK_ROOK + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.BLACK_PAWN + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.BLACK_KNIGHT + EscapeSequences.TERMINAL_BG_COLOR;
                            }else if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                                squares[row][col] = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.BLACK_BISHOP + EscapeSequences.TERMINAL_BG_COLOR;
                            }
                        }
                    }
                }

            }
        }
        return squares;
    }
    public void printChessBoard(ChessGame game, boolean isWhite, boolean isHighlighted, ChessPosition pos) {
        String[] header = {"a", "b", "c", "d", "e", "f", "g", "h"};
        ChessBoard chessBoard = game.getBoard();
        String[][] squares = initializeChessBoard(chessBoard, isWhite);
        if(isHighlighted) {squares = checkHighlight(game, squares, pos);};

        for (int row = (isWhite ? 9 : 0); (isWhite ? row >= 0 : row <= 9); row += (isWhite ? -1 : 1)) {
            for (int col = (isWhite ? 0 : 9); (isWhite ? col <= 9 : col >= 0); col += (isWhite ? 1 : -1)) {
                if (col == 9 || col == 0) {
                    if (row == 0 || row == 9) {
                        System.out.print("\u2001\u2005\u200A" + " " + "\u2001\u2005\u200A" + EscapeSequences.TERMINAL_BG_COLOR);
                    } else {
                        System.out.print(SET_TEXT_COLOR_WHITE + "\u2001\u2005\u200A" + row + "\u2001\u2005\u200A" + EscapeSequences.TERMINAL_BG_COLOR);
                    }
                } else {
                    if (row == 0 || row == 9) {
                        System.out.print(SET_TEXT_COLOR_WHITE + "\u2001" + header[col - 1] + "\u2001" + EscapeSequences.TERMINAL_BG_COLOR);
                    } else {
                        System.out.print(squares[row][col]);
                    }
                }
            }
            System.out.println(); // Move to the next row
        }
    }

    public String[][] checkHighlight(ChessGame game, String[][] squares, ChessPosition pos) {
        Collection<ChessMove> validMoves = game.validMoves(pos);
        for (ChessMove move : validMoves) {
            int row = move.getEndPosition().getRow();
            int col = move.getEndPosition().getColumn();
            if ((row + col) % 2 == 0) {
                String currStr = squares[row][col];
                String newString = SET_BG_COLOR_YELLOW + currStr.substring(EscapeSequences.SET_BG_COLOR_LIGHT_GREY.length());
                squares[row][col] = newString;
            }
            else {
                String currStr = squares[row][col];
                String newString = SET_BG_COLOR_YELLOW + currStr.substring(EscapeSequences.SET_BG_COLOR_BLACK.length());
                squares[row][col] = newString;
            }
        }
        return squares;
    }
}

