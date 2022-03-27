package chess.domain.state;

import java.util.Map;

import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.domain.command.Command;
import chess.domain.piece.Piece;

public class RunningBlackTurn extends Running {

	public RunningBlackTurn(Map<Position, Piece> pieces) {
		super(pieces);
	}

	@Override
	public State proceed(Command command) {
		if (!command.isMove()) {
			return checkFinished(command);
		}
		validateMoveOpponent(command);
		board.movePiece(command.getFromPosition(), command.getToPosition());
		return checkWhiteKingExist();
	}

	private void validateMoveOpponent(Command command) {
		if (board.isWhite(command.getFromPosition())) {
			throw new IllegalArgumentException(CANNOT_MOVE_OPPONENT_PIECE);
		}
	}

	private State checkWhiteKingExist() {
		if (board.isKingNotExist(Color.WHITE)) {
			return new Finished(this.board.getPieces());
		}
		return new RunningWhiteTurn(board.getPieces());
	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}
}
