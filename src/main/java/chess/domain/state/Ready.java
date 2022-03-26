package chess.domain.state;

import java.util.Map;

import chess.domain.board.Board;
import chess.domain.position.Position;
import chess.domain.command.Command;
import chess.domain.piece.Piece;

public class Ready extends State {

	private static final String CANNOT_MOVE = "게임 시작 전에는 움직일 수 없습니다.";

	protected Ready(Map<Position, Piece> board) {
		this.board = new Board(board);
	}

	@Override
	public State proceed(Command command) {
		if (command.isMove()) {
			throw new IllegalArgumentException(CANNOT_MOVE);
		}
		if (command.isStart()) {
			return new RunningWhiteTurn(board.getPieces());
		}
		return new Finished(board.getPieces());
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
