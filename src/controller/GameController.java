package controller;


import listener.GameListener;
import model.ChessPiece;
import model.Chessboard;
import model.ChessboardPoint;
import model.PlayerColor;
import view.AnimalChessComponent;
import view.CellComponent;
import view.ChessboardComponent;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener {


    private PlayerColor winner = null;
    private Chessboard model;
    private ChessboardComponent view;
    private PlayerColor currentPlayer;
    private int count = 1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;

    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        // Register the controller to the view
        // so that the view can call the controller's method
        // when the view receives the user's request
        // (in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece())


        view.registerController(this);
        // Initialize the chessboard
        initialize();
        view.initiateChessComponent(model);
        view.repaint();

    }

    private void initialize() {
        //这是一个初始化的private 函数
        //
//        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
//            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
//            }
//        }
        model.initPieces();
    }

    // after a valid move swap the player
    private void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
        //use this method to change the player's color,since the player is changed
    }


    private boolean win(PlayerColor currentPlayer) {
        // Check the board if there is a winner

        // Check if the current player's pieces have entered the opponent's den
        if (currentPlayer == PlayerColor.RED) {
                // Check if all pieces of the player are captured
                if (model.areAllPiecesCaptured(currentPlayer)) {
                    return true;
                }

                // Check if the player is stuck and unable to make any moves
                if (model.isPlayerStuck(currentPlayer)) {
                    return true;
                }

                // Check if the opponent's dens is occupied by the player
                if (model.isDensOccupied(PlayerColor.BLUE)) {
                    return true;
                }

                // The player has not won yet
                return false;


        } else if (currentPlayer == PlayerColor.BLUE) {
            // Check if all pieces of the player are captured
            if (model.areAllPiecesCaptured(currentPlayer)) {
                return true;
            }

            // Check if the player is stuck and unable to make any moves
            if (model.isPlayerStuck(currentPlayer)) {
                return true;
            }

            // Check if the opponent's dens is occupied by the player
            if (model.isDensOccupied(PlayerColor.RED)) {
                return true;
            }

            // The player has not won yet
            return false;
        }

        // No winner found
        return false;
    }
    public void restart() {
        model.restart();
        view.initiateChessComponent(model);
        view.repaint();
        this.currentPlayer = PlayerColor.BLUE;
        this.selectedPoint = null;
    }


    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        Chessboard chessboard = new Chessboard();
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            // System.out.println("valid move");
            //如果这个点是valid move
            //那么就把这个点的棋子移动到这个点
            //然后把这个点的棋子的component移动到这个点
            //然后把selectedPoint设为null
            //然后swapColor
            //然后repaint

            model.moveChessPiece(selectedPoint, point);

            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            swapColor();
            view.repaint();
            // TODO: if the chess enter Dens or Traps and so on
            ChessPiece pointPiece = model.getChessPieceAt(point);
            if (pointPiece.getName().equals("Trap")
                    && ((this.currentPlayer.equals(PlayerColor.BLUE) && point.getRow() < 3)
                    || (this.currentPlayer.equals(PlayerColor.RED) && point.getRow() > 6))) {
                this.model.getChessPieceAt(point).setRank(0);
            }
            if (pointPiece.getName().equals("Den")) {
                winner = currentPlayer;
            }

            chessboard.printChessNotation();
            int roundCount = chessboard.getRoundCount();
            System.out.println("Round count: " + roundCount);
        }
    }

    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, AnimalChessComponent component) {

        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.revalidate();
                component.repaint();
            }
            //如果这个点是空的
            //那么就把这个点设为selectedPoint
            //然后把这个点的棋子的component设为selected
            //然后repaint
            //然后return

        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);
            component.repaint();
            //如果这个点是selectedPoint
            //那么就把selectedPoint设为null
            //然后把这个点的棋子的component设为unselected
            //然后repaint
            //然后return

        }
        // TODO: Implement capture function
        if (model.isValidCapture(selectedPoint,point)){

        }


    }
}
