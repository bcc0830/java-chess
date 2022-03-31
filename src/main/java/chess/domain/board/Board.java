package chess.domain.board;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import chess.domain.direction.Direction;
import chess.domain.position.Position;

public class Board {

    private static final String NO_PIECE = "해당 위치에 말이 없습니다.";
    private static final String PIECE_BLOCK = "가려는 위치 중간에 말이 존재합니다.";
    private static final String CANNOT_MOVE_SAME_COLOR = "아군이 있는 위치에 갈 수 없습니다.";
    private static final String PAWN_ONLY_DIAGONAL_CATCH = "폰은 본인 진행 방향 대각선에 있는 적만 잡을 수 있습니다.";
    private static final String CANNOT_MOVE_DIAGONAL_NOT_ENEMY = "폰은 적이 없으면 대각선으로 갈 수 없습니다.";

    private static final int CRITERIA_COUNT_OF_PAWN_SCORE = 1;

    private final Map<Position, Piece> pieces;

    public Board(Map<Position, Piece> pieces) {
        this.pieces = new HashMap<>(pieces);
    }

    public boolean isSameColor(Position position, Color color) {
        return checkFromPieceEmpty(position).isSameColor(color);
    }

    public Optional<Piece> findPiece(Position position) {
        return pieces.keySet().stream()
                .filter(pos -> pos.equals(position))
                .map(this.pieces::get)
                .findFirst();
    }

    public void movePiece(Position from, Position to) {
        Piece fromPiece = checkFromPieceEmpty(from);
        Direction direction = fromPiece.matchDirection(from, to);
        validateBlockingPiece(from, to);
        checkTargetPosition(to, fromPiece, direction);

        move(from, to, fromPiece);
    }

    private Piece checkFromPieceEmpty(Position from) {
        Optional<Piece> piece = findPiece(from);
        if (piece.isEmpty()) {
            throw new NoSuchElementException(NO_PIECE);
        }
        return piece.get();
    }

    private void validateBlockingPiece(Position from, Position to) {
        List<Position> path = from.backtrackPath(to);
        for (Position position : path) {
            validateExistPiece(position);
        }
    }

    private void validateExistPiece(Position now) {
        if (findPiece(now).isPresent()) {
            throw new IllegalArgumentException(PIECE_BLOCK);
        }
    }

    private void checkTargetPosition(Position to, Piece fromPiece, Direction direction) {
        Optional<Piece> piece = findPiece(to);
        if (piece.isPresent()) {
            Piece toPiece = piece.get();
            validateSameColor(fromPiece, toPiece);
            checkPieceIsPawn(fromPiece, direction, toPiece);
            return;
        }
        validatePawnDiagonalMove(fromPiece, direction);
    }

    private void validateSameColor(Piece fromPiece, Piece toPiece) {
        if (fromPiece.isSameColor(toPiece)) {
            throw new IllegalArgumentException(CANNOT_MOVE_SAME_COLOR);
        }
    }

    private void checkPieceIsPawn(Piece fromPiece, Direction direction, Piece toPiece) {
        if (fromPiece.isPawn()) {
            validateDiagonalEnemy(fromPiece, toPiece, direction);
        }
    }

    private void validateDiagonalEnemy(Piece fromPiece, Piece toPiece, Direction direction) {
        if (!direction.isDiagonal() || fromPiece.isSameColor(toPiece)) {
            throw new IllegalArgumentException(PAWN_ONLY_DIAGONAL_CATCH);
        }
    }

    private void validatePawnDiagonalMove(Piece fromPiece, Direction direction) {
        if (fromPiece.isPawn() && direction.isDiagonal()) {
            throw new IllegalArgumentException(CANNOT_MOVE_DIAGONAL_NOT_ENEMY);
        }
    }

    private void move(Position from, Position to, Piece piece) {
        this.pieces.remove(from);
        this.pieces.put(to, piece);
    }

    public boolean isKingNotExist(Color color) {
        return this.pieces.values().stream()
                .noneMatch(piece -> piece.isKing() && piece.isSameColor(color));
    }

    public Map<Position, Piece> getPieces() {
        return new HashMap<>(pieces);
    }
}
