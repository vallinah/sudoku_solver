import { useEffect, useState } from "react";
import axios from "axios";

function App() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/health")
      .then((response) => {
        setMessage(response.data);
      })
      .catch((error) => {
        console.error(error);
        setMessage("Erreur de connexion au backend");
      });
  }, []);

  return (
    <>
      <h1>Sudoku Solver</h1>
      <p>{message}</p>
    </>
  );
}

export default App;