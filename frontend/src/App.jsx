import { useState } from "react";

function App() {
  const emptyGrid = () =>
    Array(9)
      .fill(null)
      .map(() => Array(9).fill(0));

  const [grid, setGrid] = useState(emptyGrid());
  const [initialGrid, setInitialGrid] = useState(emptyGrid());
  const [currentStep, setCurrentStep] = useState(null);

  const handleChange = (rowIndex, colIndex, value) => {
    // Autoriser seulement vide ou chiffre 1-9
    if (value !== "" && !/^[1-9]$/.test(value)) {
      return;
    }

    setGrid((previousGrid) => {
      const newGrid = previousGrid.map((row) => [...row]);

      newGrid[rowIndex][colIndex] =
        value === "" ? 0 : Number(value);

      return newGrid;
    });
  };

  const resetGrid = () => {
    const newGrid = emptyGrid();

    setGrid(newGrid);
    setInitialGrid(newGrid);
    setCurrentStep(null);
  };

  const solve = async () => {
    // Sauvegarder la grille AVANT la résolution
    const startingGrid = grid.map((row) => [...row]);

    setInitialGrid(startingGrid);

    const response = await fetch(
      "http://localhost:8080/api/sudoku/solve",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          grid: startingGrid,
        }),
      }
    );

    const data = await response.json();

    playSteps(data.steps, startingGrid);
  };

  const sleep = (ms) =>
    new Promise((resolve) => setTimeout(resolve, ms));

  const playSteps = async (steps, startingGrid) => {
    let currentGrid = startingGrid.map((row) => [...row]);

    for (const step of steps) {
      currentGrid[step.row][step.col] = step.value;

      setGrid(currentGrid.map((row) => [...row]));

      setCurrentStep(step);

      await sleep(500);
    }

    setCurrentStep(null);
  };

  const loadExample = () => {
    const example = [
      [5,3,0,0,7,0,0,0,0],
      [6,0,0,1,9,5,0,0,0],
      [0,9,8,0,0,0,0,6,0],
  
      [8,0,0,0,6,0,0,0,3],
      [4,0,0,8,0,3,0,0,1],
      [7,0,0,0,2,0,0,0,6],
  
      [0,6,0,0,0,0,2,8,0],
      [0,0,0,4,1,9,0,0,5],
      [0,0,0,0,8,0,0,7,9]
    ];

    const easy = [
      [6,0,5,0,0,0,4,0,7],
      [0,3,0,5,0,0,0,0,6],
      [0,1,9,0,6,0,0,0,0],
      [0,2,0,8,7,0,3,5,0],
      [0,5,1,3,4,9,7,0,0],
      [4,7,0,2,0,6,0,0,0],
      [1,0,7,0,0,3,0,4,0],
      [0,0,0,9,1,0,0,0,8],
      [0,6,8,0,2,7,9,0,1]
    ];

    const middle = [
      [0,8,0,3,0,0,0,0,0],
      [0,0,6,8,0,0,0,7,4],
      [1,5,0,0,0,0,0,8,0],
      [0,0,0,0,0,0,0,6,0],
      [0,0,1,9,0,4,0,2,0],
      [6,7,2,0,0,3,4,9,8],
      [0,2,0,4,0,0,1,0,0],
      [0,0,0,0,1,0,0,0,0],
      [4,1,0,0,9,6,0,0,0]
     ];

    const newGrid = middle.map((row) => [...row]);

    setGrid(newGrid);
    setInitialGrid(newGrid);
    setCurrentStep(null);
  };

  console.log(grid);

  return (
    <div>
      <h1>Sudoku Solver</h1>

      <div className="sudoku-container">
        <div className="sudoku-grid">
          {grid.map((row, rowIndex) =>
            row.map((cell, colIndex) => {
              const isGiven =
                initialGrid[rowIndex][colIndex] !== 0;

              const isSolved =
                initialGrid[rowIndex][colIndex] === 0 &&
                cell !== 0;

              const isCurrentStep =
                currentStep &&
                currentStep.row === rowIndex &&
                currentStep.col === colIndex;

              return (
                <input
                  key={`${rowIndex}-${colIndex}`}
                  className={`
                    cell
                    ${isGiven ? "given-cell" : ""}
                    ${isSolved ? "solved-cell" : ""}
                    ${isCurrentStep ? "current-step" : ""}
                    ${colIndex % 3 === 0 ? "left-border" : ""}
                    ${rowIndex % 3 === 0 ? "top-border" : ""}
                    ${colIndex === 8 ? "right-border" : ""}
                    ${rowIndex === 8 ? "bottom-border" : ""}
                  `}
                  value={cell === 0 ? "" : cell}
                  onChange={(e) =>
                    handleChange(
                      rowIndex,
                      colIndex,
                      e.target.value
                    )
                  }
                  maxLength="1"
                />
              );
            })
          )}
        </div>

        <div className="buttons">
          <button onClick={solve}>
            Résoudre
          </button>

          <button onClick={resetGrid}>
            Réinitialiser
          </button>

          <button onClick={loadExample}>
            Charger un exemple
          </button>
        </div>
      </div>
    </div>
  );
}

export default App;