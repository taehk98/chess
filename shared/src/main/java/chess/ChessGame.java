package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import chess.ChessPiece.PieceType;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private TeamColor currentTurn = TeamColor.WHITE;
    private boolean hasWhiteKingMoved = false;
    private boolean hasBlackKingMoved = false;
    private boolean hasWhite1RookMoved = false;
    private boolean hasWhite8RookMoved = false;
    private boolean hasBlack1RookMoved = false;
    private boolean hasBlack8RookMoved = false;

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null) {
            return moves;
        }

        Collection<ChessMove> unfilteredMoves = piece.pieceMoves(board, startPosition);

        for(ChessMove move : unfilteredMoves){
            ChessPiece movedPlaceOriPiece = board.getPiece(move.getEndPosition());

            // Make temporary move
            board.addPiece(move.getEndPosition(), piece);
            board.deletePiece(move.getStartPosition());

            // Check if the move is valid
            boolean isValidMove = !isInCheck(piece.getTeamColor());

            // Undo the temporary move
            board.addPiece(move.getStartPosition(), board.getPiece(move.getEndPosition()));
            board.deletePiece(move.getEndPosition());

            if (isValidMove) {
                moves.add(move);
            }

            if (movedPlaceOriPiece != null) {
                // Add back the original piece only if it was there
                board.addPiece(move.getEndPosition(), movedPlaceOriPiece);
            }
        }
        if(moves.isEmpty() && piece.getPieceType() == PieceType.KING){
            moves.add(new ChessMove(startPosition, startPosition, null));
        }
        return moves;
    }

    public HashSet<ChessMove> castlingMove(HashSet<ChessMove> moves, ChessPosition startPosition){
        ChessPiece currPiece = board.getPiece(startPosition);
        if(currPiece.getTeamColor() == TeamColor.WHITE){
            if(!hasWhite1RookMoved && !hasWhiteKingMoved && isInCheck(currPiece.getTeamColor())){

            }
        }

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(validMoves(move.getStartPosition()).contains(move) && piece != null
                && piece.getTeamColor() == currentTurn){
            if(piece.getPieceType() == PieceType.PAWN && (move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8)){
                board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            }
            else{
                board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
            }
            board.deletePiece(move.getStartPosition());
            currentTurn = (currentTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
            ChessPiece movedPiece = board.getPiece(move.getEndPosition());
            if(movedPiece.getPieceType() == PieceType.KING){
                if(movedPiece.getTeamColor() == TeamColor.WHITE){
                    hasWhiteKingMoved = true;
                }else{
                    hasBlackKingMoved = true;
                }
            }else if(movedPiece.getPieceType() == PieceType.ROOK
                    && move.getStartPosition().getColumn() == 1){
                if(movedPiece.getTeamColor() == TeamColor.WHITE){
                    hasWhite1RookMoved = true;
                }else{
                    hasBlack1RookMoved = true;
                }
            }else if(movedPiece.getPieceType() == PieceType.ROOK
                    && move.getStartPosition().getColumn() == 8){
                if(movedPiece.getTeamColor() == TeamColor.WHITE){
                    hasWhite8RookMoved = true;
                }else{
                    hasBlack8RookMoved = true;
                }
            }
        }
        else{
            throw new InvalidMoveException("Invalid chess move");
        }
        
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = null;

        // 왕의 위치 찾기
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition currentPos = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(currentPos);

                if (piece != null && piece.getTeamColor() == teamColor
                        && piece.getPieceType() == PieceType.KING) {
                    kingPos = currentPos;
                    break;  // 왕을 찾았으면 반복 종료
                }
            }
            if (kingPos != null) {
                break;  // 왕을 찾았으면 외부 반복 종료
            }
        }

        // 왕이 없다면 체크 상태가 아님
        if (kingPos == null) {
            return false;
        }


        // 왕에 대해 공격 가능한 위치 확인
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition attPos = new ChessPosition(i, j);
                ChessPiece attPiece = board.getPiece(attPos);

                if (attPiece != null && attPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = attPiece.pieceMoves(board, attPos);

                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPos)) {
                            return true;  // 왕에 대한 공격 발견, 체크 상태
                        }
                    }
                }
            }
        }

        return false;  // 모든 검사를 마쳤는데도 체크가 발견되지 않음
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // 해당 팀이 체크 상태에 있는지 확인
        if (!isInCheck(teamColor)) {
            return false;
        }

        ChessPosition kingPos = null;

        // 왕의 위치 찾기
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition currentPos = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(currentPos);

                if ((piece != null) && (piece.getTeamColor().equals(teamColor))
                        && (piece.getPieceType() == PieceType.KING)) {
                    kingPos = currentPos;
                    break;  // 왕을 찾았으면 반복 종료
                }
            }
            if (kingPos != null) {
                break;  // 왕을 찾았으면 외부 반복 종료
            }
        }


        for (int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);

                if(piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> moves = validMoves(pos);
                    if (piece.getPieceType() == PieceType.KING && moves.size() != 1) {
                        // 킹이면서 이동 가능한 위치가 1개 초과인 경우
                        return false;
                    } else if (piece.getPieceType() != PieceType.KING && !moves.isEmpty()) {
                        // 이동 가능한 위치가 없는 경우
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        if(currentTurn != teamColor){
            return false;
        }

        for (int r = 1; r <= 8; r++){
            for(int c = 1; c <= 8; c++){
                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(pos);

                if(piece != null && piece.getTeamColor().equals(teamColor)){
                    Collection<ChessMove> moves = validMoves(pos);
                    if(moves != null && moves.isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
