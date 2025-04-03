import { CssBaseline, ThemeProvider } from "@mui/material";
import "./App.css";
import { DarkTheme } from "./theme/DarkTheme";
import { BrowserRouter as Router } from "react-router-dom";
import { MainRouter } from "./router/MainRouter";

function App() {
  return (
    <Router>
      <ThemeProvider theme={DarkTheme}>
        <CssBaseline />
        <MainRouter />
      </ThemeProvider>
    </Router>
  );
}

export default App;
